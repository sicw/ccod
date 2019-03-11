package com.channelsoft.common.util.rocket.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class ProducerTest {
    @Test
    public void testProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("DefaultProducerGroup");
        producer.setNamesrvAddr("10.130.41.36:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TopicTest", ("TopicA1_1 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
            Thread.sleep(2000);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
