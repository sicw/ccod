package com.channelsoft.ucds.rocket;

import com.channelsoft.common.util.rocketmq.RocketMqUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class MqConsumerClusterTest {
    @Test
    public void testConsumerCluster() throws IOException {
        for (int i = 0; i < 1; i++) {
            RocketMqUtil.registryConsumer("DefaultConsumerGroup", "10.130.41.36:9876", "TopicSelector", new SimpleMessageListenerOrderly());
        }
        RocketMqUtil.startConsumer();
        System.in.read();
    }

    private class SimpleMessageListenerOrderly implements MessageListenerOrderly {
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            System.out.println(Thread.currentThread().getName() + ": " + context.getMessageQueue().getQueueId() + " :" + new String(msgs.get(0).getBody()));
            return ConsumeOrderlyStatus.SUCCESS;
        }
    }
}
