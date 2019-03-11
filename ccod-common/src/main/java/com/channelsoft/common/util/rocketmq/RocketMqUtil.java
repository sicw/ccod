package com.channelsoft.common.util.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * 包含了生产者和消费者的工具
 * @author sicwen
 * @date 2019/03/07
 */
public class RocketMqUtil {
    private final static Logger logger = LoggerFactory.getLogger(RocketMqUtil.class);
    private static DefaultMQProducer producer;
    private static ArrayList<DefaultMQPushConsumer> consumers = new ArrayList<DefaultMQPushConsumer>();

    static {
        try {
            //provider
            producer = new DefaultMQProducer("DefaultProducerGroup");
            producer.setNamesrvAddr("10.130.41.36:9876");
            producer.start();
            // consumer
            // 先注册，然后手动启动
        } catch (Exception e) {
            producer.shutdown();
            logger.error("connection rocketmq error {}", e);
        }
    }

    public static boolean send(String topic, byte[] body) {
        Message message = new Message(topic, "", "", 0, body, true);
        return doSend(message);
    }

    public static boolean send(String topic, String tag, String keys, int flag, byte[] body, boolean waitStoreMsgOK) {
        Message message = new Message(topic, tag, keys, flag, body, waitStoreMsgOK);
        return doSend(message);
    }

    /**
     * 这里如果发送失败可以加一些策略
     *
     * @param message
     * @return
     */
    public static boolean doSend(Message message) {
        SendResult sr = null;
        try {
            sr = producer.send(message);
        } catch (Exception e) {
            logger.error("send msg to rocketmq exception {}", e.getCause());
        }
        if (sr != null) {
            if (sr.getSendStatus() == SendStatus.SEND_OK) {
                return true;
            } else {
                logger.error("send msg to rocketmq error {}", sr.getSendStatus());
                return false;
            }
        }
        return false;
    }

    public static void registryConsumer(String groupName, String mqAddress, String topic, MessageListenerConcurrently handler){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(mqAddress);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        try {
            consumer.subscribe(topic,"*");
        } catch (MQClientException e) {
            logger.error("consumer subscribe topic error {}",e);
        }
        consumer.registerMessageListener(handler);
        consumers.add(consumer);
    }

    public static void startConsumer(){
        for (DefaultMQPushConsumer consumer : consumers) {
            try {
                consumer.start();
            } catch (MQClientException e) {
                logger.error("consumers start error {}",e);
            }
        }
    }
}