package com.channelsoft.ucds.consumers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageMakeCall;
import com.channelsoft.common.redis.JedisUtil;
import com.channelsoft.common.reply.ReplyMakeCall;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/13
 */
public class UcdsMessageListener implements MessageListenerOrderly {

    private static final Logger logger = LoggerFactory.getLogger(UcdsMessageListener.class);

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            String tag = msg.getTags();
            logger.info("ucds receive a tag {}",tag);
            if ("replyMakeCall".equals(tag)){
                String messageBody = new String(msg.getBody());
                ReplyMakeCall replyMakeCall = null;
                try {
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    replyMakeCall = JSONObject.toJavaObject(jsonObject, ReplyMakeCall.class);
                    String sessionId = replyMakeCall.getSessionId();
                    int causeCode = replyMakeCall.getCauseCode();
                    String cause = replyMakeCall.getCause();
                    if(causeCode == 0){
                        JedisUtil.getRedisClient().hset(sessionId,"callerstatus","calling");
                        JedisUtil.getRedisClient().hset(sessionId,"calledstatus","calling");
                    }else if (causeCode == 10){
                        JedisUtil.getRedisClient().hset(sessionId,"callerstatus","failed");
                        JedisUtil.getRedisClient().hset(sessionId,"calledstatus","calling");
                    }else if (causeCode == 11){
                        JedisUtil.getRedisClient().hset(sessionId,"callerstatus","calling");
                        JedisUtil.getRedisClient().hset(sessionId,"calledstatus","failed");
                    }
                }catch (Exception e){
                    logger.error("ucds receive replyMakeCall exception {}",e);
                }
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
