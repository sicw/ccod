package com.channelsoft.cms.consumers.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageMakeCall;
import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import com.channelsoft.common.service.ServiceMakeCall;
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

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            String tag = msg.getTags();
            if(tag.equals("messageMakeCall")){
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
                RocketmqUtil.sendUpStream(replyMakeCall.getSessionId(),"replyMakeCall",json.getBytes());
                logger.info("cms send {}",replyMakeCall);
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
