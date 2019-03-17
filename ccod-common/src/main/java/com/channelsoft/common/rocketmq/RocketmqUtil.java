package com.channelsoft.common.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sicwen
 * @date 2019/03/13
 */
public class RocketmqUtil {

    private static final Logger logger = LoggerFactory.getLogger(RocketmqUtil.class);
    private static DefaultMQProducer producer = null;

    static{
        producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr("10.130.41.36:9876");
        producer.setInstanceName("Producer");
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("create rocketmq producer exception {}",e);
        }
    }

    public static void send(String sessionId,String tag,byte[] body){
        Message msg = new Message("CCOD_SESSION_TOPIC_1",tag,"",0,body,true);
        try {
            producer.send(msg,new SelectMessageQueueByHash(),sessionId);
        } catch (Exception e) {
            logger.error("producer send message exception {}",e);
        }
    }

    public static void sendUpStream(String sessionId,String tag,byte[] body){
        Message msg = new Message("CCOD_SESSION_TOPIC_2",tag,"",0,body,true);
        try {
            producer.send(msg,new SelectMessageQueueByHash(),sessionId);
        } catch (Exception e) {
            logger.error("producer send message exception {}",e);
        }
    }

    public static void createConsumer(MessageListenerOrderly messageListener){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CmsConsumer");
        consumer.setNamesrvAddr("10.130.41.36:9876");
        consumer.setInstanceName("CmsConsumer1");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeThreadMin(1);
        consumer.setConsumeThreadMax(1);
        consumer.registerMessageListener(messageListener);
        try {
            consumer.subscribe("CCOD_SESSION_TOPIC_1", "*");
            consumer.start();
        } catch (MQClientException e) {
            logger.error("create consumer exception {}",e);
        }
    }

    public static void createConsumerUpStream(MessageListenerOrderly messageListener){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("UcdsConsumer");
        consumer.setNamesrvAddr("10.130.41.36:9876");
        consumer.setInstanceName("UcdsConsumer1");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeThreadMin(1);
        consumer.setConsumeThreadMax(1);
        consumer.registerMessageListener(messageListener);
        try {
            consumer.subscribe("CCOD_SESSION_TOPIC_2", "*");
            consumer.start();
        } catch (MQClientException e) {
            logger.error("create consumer exception {}",e);
        }
    }
}