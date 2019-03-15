package com.channelsoft.common.rocketmq.support;

import com.channelsoft.common.rocketmq.ConsumerFactory;
import com.channelsoft.common.rocketmq.TagAndMessageHandler;
import com.channelsoft.common.rocketmq.TopicInfo;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class CacheConsumerFactoryImpl implements ConsumerFactory {

    private final Logger logger = LoggerFactory.getLogger(CacheConsumerFactoryImpl.class);

    private String nameSrvAddress;
    private List<TagAndMessageHandler> tagAndMessageHandlers;
    private CopyOnWriteArrayList<TopicInfo> topicInfos = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<DefaultMQPushConsumer> consumers = new CopyOnWriteArrayList<>();

    public void createConsumer(String topic,String tag,MessageListenerOrderly handle) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(topic + "-" + tag + "-Consumer");
        consumer.setNamesrvAddr(nameSrvAddress);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.registerMessageListener(handle);
        consumer.subscribe(topic,tag);
        consumer.start();
        consumers.add(consumer);
    }

    public void refreshTopicInfo(){
        //获取Topic信息
        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setTopicName("CCOD_SESSION_TOPIC_1");
        topicInfo.setReadQueueSize(16);
        topicInfo.setWirteQueueSize(16);
        topicInfo.setPerm(6);
        //缓存Topic信息
        topicInfos.add(topicInfo);
    }

    @Override
    public void refreshConsumers() {
        refreshTopicInfo();
        for (TopicInfo topicInfo : topicInfos) {
            for (TagAndMessageHandler tagAndMessageHandler : tagAndMessageHandlers) {
                try {
                    createConsumer(topicInfo.getTopicName(),tagAndMessageHandler.getTag(),tagAndMessageHandler.getMessageListenerOrderly());
                } catch (MQClientException e) {
                    logger.error("create "+topicInfo.getTopicName()+" consumer exception {}",e);
                }
            }
        }
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setTagAndMessageHandlers(List<TagAndMessageHandler> tagAndMessageHandlers) {
        this.tagAndMessageHandlers = tagAndMessageHandlers;
    }

    public List<TagAndMessageHandler> getTagAndMessageHandlers() {
        return tagAndMessageHandlers;
    }
}
