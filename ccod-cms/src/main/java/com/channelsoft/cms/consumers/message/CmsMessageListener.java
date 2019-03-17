package com.channelsoft.cms.consumers.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageDropCall;
import com.channelsoft.common.message.MessageMakeCall;
import com.channelsoft.common.message.MessageRecord;
import com.channelsoft.common.reply.ReplyDropCall;
import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.reply.ReplyRecord;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import com.channelsoft.common.service.ServiceDropCall;
import com.channelsoft.common.service.ServiceMakeCall;
import com.channelsoft.common.service.ServiceRecord;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/13
 */
@Service
public class CmsMessageListener implements MessageListenerOrderly {

    private Logger logger = LoggerFactory.getLogger(CmsMessageListener.class);

    @Autowired
    private ServiceMakeCall serviceMakeCall;

    @Autowired
    private ServiceRecord serviceRecord;

    @Autowired
    private ServiceDropCall serviceDropCall;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            String tag = msg.getTags();
            String messageBody = new String(msg.getBody());
            if(tag.equals("messageMakeCall")){
                MessageMakeCall messageMakeCall = null;
                try {
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    messageMakeCall = JSONObject.toJavaObject(jsonObject, MessageMakeCall.class);
                    logger.info("cms receive MessageMakeCall {}",messageMakeCall);
                }catch (Exception e){
                    logger.error("cms convert json to MessageMakeCall error {}",messageBody);
                    continue;
                }

                //引用Dubbo服务
                ReplyMakeCall replyMakeCall = null;
                try {
                    replyMakeCall = serviceMakeCall.makeCall(messageMakeCall.getSessionId(), messageMakeCall.getCaller(), messageMakeCall.getCalled(), messageMakeCall.getExtMap());
                    logger.info("cms invoke ServiceMakeCall success {}",replyMakeCall);
                }catch (Exception e){
                    logger.error("cms invoke ServiceMakeCall Exception {}",e);
                    continue;
                }

                //返回结果给mq
                String json = JSONObject.toJSONString(replyMakeCall);
                RocketmqUtil.sendUpStream(replyMakeCall.getSessionId(),"replyMakeCall",json.getBytes());
                logger.info("cms send {}",replyMakeCall);
            }else if ("messageRecord".equals(tag)){
                MessageRecord messageRecord = null;
                try {
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    messageRecord = JSONObject.toJavaObject(jsonObject, MessageRecord.class);
                    logger.info("cms receive {}", messageRecord);
                }catch (Exception e){
                    logger.error("cms convert json to MessageRecord error {}",messageBody);
                    continue;
                }

                //引用Dubbo服务
                ReplyRecord replyRecord = null;
                try {
                    replyRecord = serviceRecord.record(messageRecord.getSessionId(),messageRecord.getSide(),messageRecord.getRecordFileName(),messageRecord.getExtMap());
                    logger.info("cms invoke ServiceRecord success {}",replyRecord);
                }catch (Exception e){
                    logger.error("cms invoke ServiceRecord Exception {}",e);
                    continue;
                }

                //返回结果给mq
                String json = JSONObject.toJSONString(replyRecord);
                RocketmqUtil.sendUpStream(replyRecord.getSessionId(),"replyRecord",json.getBytes());
                logger.info("cms send {}",replyRecord);
            } else if ("messageDropCall".equals(tag)){
                MessageDropCall messageDropCall = null;
                try{
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    messageDropCall = JSONObject.toJavaObject(jsonObject,MessageDropCall.class);
                    logger.info("cms receive {}",messageDropCall);
                }catch (Exception e){
                    logger.error("cms convert json to MessageDropCall error {}",e);
                }

                //调用Dubbo服务
                ReplyDropCall replyDropCall = null;
                try {
                    replyDropCall = serviceDropCall.dropCall(messageDropCall.getSessionId(),messageDropCall.getSide(),messageDropCall.getExtMap());
                    logger.info("cms invoke MessageDropCall success {}",replyDropCall);
                }catch (Exception e){
                    logger.error("cms invoke DropCall error {}",e);
                }

                //发送结果
                String json = JSONObject.toJSONString(replyDropCall);
                RocketmqUtil.sendUpStream(replyDropCall.getSessionId(),"replyDropCall",json.getBytes());
                logger.info("cms send reply {}",replyDropCall);
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}