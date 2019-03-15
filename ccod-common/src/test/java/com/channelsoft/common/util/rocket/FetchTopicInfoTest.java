package com.channelsoft.common.util.rocket;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.producer.TopicPublishInfo;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class FetchTopicInfoTest {
    @Test
    public void fetchTopicInfo(){
        DefaultMQProducer producer = new DefaultMQProducer("DefaultProducerGroup");
        producer.setNamesrvAddr("10.130.41.36:9876");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        int queueNums = producer.getDefaultTopicQueueNums();
        TopicPublishInfo info = new TopicPublishInfo();
        try {
            List<MessageQueue> list = producer.fetchPublishMessageQueues("CCOD_SESSION_TOPIC_2");
            System.out.println(list.size());
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
