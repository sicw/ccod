package com.channelsoft.common.util.rocketmq;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class MessageQueueSelectorWithSessionId implements MessageQueueSelector {

    private static int count = 0;

    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        if (arg instanceof String){
            String sessionId = (String) arg;
            int id = Integer.parseInt(sessionId);
            int mqSize = mqs.size();
            int index = id % mqSize;
            return mqs.get(index);
        }else{
            count++;
            int index = count % mqs.size();
            return mqs.get(index);
        }
    }
}
