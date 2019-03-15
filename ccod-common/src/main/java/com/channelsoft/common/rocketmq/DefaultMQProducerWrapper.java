package com.channelsoft.common.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class DefaultMQProducerWrapper {
    private DefaultMQProducer defaultMQProducer;
    private String topicName;
    private MessageQueueSelector mqSelector;
    private Object selectArg;

    public DefaultMQProducerWrapper(DefaultMQProducer producer,String topicName){
        this.defaultMQProducer = producer;
        this.topicName = topicName;
    }

    public void setDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setMqSelector(MessageQueueSelector mqSelector) {
        this.mqSelector = mqSelector;
    }

    public MessageQueueSelector getMqSelector() {
        return mqSelector;
    }

    public void setSelectArg(Object selectArg) {
        this.selectArg = selectArg;
    }

    public Object getSelectArg() {
        return selectArg;
    }
}