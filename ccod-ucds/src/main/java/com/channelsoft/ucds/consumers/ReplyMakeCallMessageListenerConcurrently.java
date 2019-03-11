package com.channelsoft.ucds.consumers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.reply.ReplyMakeCall;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class ReplyMakeCallMessageListenerConcurrently implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(ReplyMakeCallMessageListenerConcurrently.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            try {
                JSONObject jsonObject = JSON.parseObject(new String(msg.getBody()));
                ReplyMakeCall replyMakeCall = JSONObject.toJavaObject(jsonObject, ReplyMakeCall.class);
                logger.info("ucds receive a reply {}", replyMakeCall);
            }catch (Exception e){
                logger.error("ucds receive replyMakeCall error {}",e);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
