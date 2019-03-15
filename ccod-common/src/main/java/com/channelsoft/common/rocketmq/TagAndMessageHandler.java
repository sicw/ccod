package com.channelsoft.common.rocketmq;

import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class TagAndMessageHandler {
    private String tag;
    private MessageListenerOrderly messageListenerOrderly;

    public TagAndMessageHandler() {
    }

    public TagAndMessageHandler(String tag, MessageListenerOrderly messageListenerOrderly) {
        this.tag = tag;
        this.messageListenerOrderly = messageListenerOrderly;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public MessageListenerOrderly getMessageListenerOrderly() {
        return messageListenerOrderly;
    }

    public void setMessageListenerOrderly(MessageListenerOrderly messageListenerOrderly) {
        this.messageListenerOrderly = messageListenerOrderly;
    }
}
