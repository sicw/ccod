package com.channelsoft.common.rocketmq;

import com.channelsoft.common.rocketmq.support.SelectProducerByHashImpl;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * 包含了生产者和消费者的工具
 * @author sicwen
 * @date 2019/03/07
 */
public class RocketMqManager {

    private final static Logger logger = LoggerFactory.getLogger(RocketMqManager.class);

    private ArrayList<DefaultMQPushConsumer> consumers = new ArrayList<DefaultMQPushConsumer>();

    private SelectProducer selectProducer = new SelectProducerByHashImpl();

    private ProducerFactory producerFactory;

    private ConsumerFactory consumerFactory;

    public void setSelectProducer(SelectProducer selectProducer) {
        this.selectProducer = selectProducer;
    }

    public SelectProducer getSelectProducer() {
        return selectProducer;
    }

    public void setProducerFactory(ProducerFactory producerFactory) {
        this.producerFactory = producerFactory;
    }

    public ProducerFactory getProducerFactory() {
        return producerFactory;
    }

    public void setConsumerFactory(ConsumerFactory consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    public ConsumerFactory getConsumerFactory() {
        return consumerFactory;
    }

    public  boolean send(String sessionId, String tags, byte[] body) throws Exception{
        DefaultMQProducerWrapper producerWrapper = selectProducer.selectOne(producerFactory.getActiveProducer(),sessionId);
        String bindTopicName = producerWrapper.getTopicName();
        //create Message
        Message message = new Message(bindTopicName,tags,"",0,body,true);
        //send message
        SendResult sendResult = doSend(producerWrapper,message);
        SendStatus sendStatus = sendResult.getSendStatus();
        if(sendStatus != SendStatus.SEND_OK){
            //handle fail
            handleFail(sendResult);
            return false;
        }
        return true;
    }

    public static SendResult doSend(DefaultMQProducerWrapper producerWrapper,Message message) throws Exception {
        MessageQueueSelector mqSelector = producerWrapper.getMqSelector();
        Object arg = producerWrapper.getSelectArg();
        DefaultMQProducer defaultMQProducer = producerWrapper.getDefaultMQProducer();
        SendResult sr = null;
        if (mqSelector != null) {
            sr = defaultMQProducer.send(message, mqSelector, arg);
        } else {
            sr = defaultMQProducer.send(message);
        }
        return sr;
    }

    /**
     * TODO 发送失败处理
     * @param sr
     */
    private static void handleFail(SendResult sr){
        switch (sr.getSendStatus()){
            case FLUSH_DISK_TIMEOUT:
                break;
            case SLAVE_NOT_AVAILABLE:
                break;
            case FLUSH_SLAVE_TIMEOUT:
                break;
            default:
                break;
        }
    }
}