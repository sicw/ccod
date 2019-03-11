package com.channelsoft.cms.consumers.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageMakeCall;
import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.service.ServiceMakeCall;
import com.channelsoft.common.util.rocketmq.RocketMqUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/07
 */
@Service
public class MakeCallMessageListenerConcurrently implements MessageListenerConcurrently {

    private Logger logger = LoggerFactory.getLogger(MakeCallMessageListenerConcurrently.class);

    @Autowired
    private ServiceMakeCall serviceMakeCall;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        //接受服务消息
        for (MessageExt msg : msgs) {
            String messageBody = new String(msg.getBody());
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
            boolean isOk = RocketMqUtil.send("replyMakeCall",json.getBytes());
            if (isOk) {
                logger.info("cms send ReplyMakeCall success {}",replyMakeCall);
            }else{
                logger.error("cms send ReplyMakeCall failed {}",replyMakeCall);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
