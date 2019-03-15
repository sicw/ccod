package com.channelsoft.common.rocketmq.support;

import com.channelsoft.common.rocketmq.DefaultMQProducerWrapper;
import com.channelsoft.common.rocketmq.ProducerFactory;
import com.channelsoft.common.rocketmq.TopicInfo;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class CacheProducerFactoryImpl implements ProducerFactory {

    private final Logger logger = LoggerFactory.getLogger(CacheProducerFactoryImpl.class);

    private String nameServerAddress = "localhost:9876";
    private int retyTimesWhenSendFailed = 3;
    private static int producerIndex = 1;
    private CopyOnWriteArrayList<TopicInfo> topicInfos = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<DefaultMQProducerWrapper> activeProducers = new CopyOnWriteArrayList<>();

    /**
     * TODO:更新Topic信息
     */
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

    /**
     * TODO:更新Producer信息
     */
    @Override
    public void refreshProducers() {
        refreshTopicInfo();
        //创建producer
        for (TopicInfo topicInfo : topicInfos) {
            try {
                createProducer(topicInfo);
            } catch (MQClientException e) {
                logger.error("create "+topicInfo.getTopicName()+" producer error {}",e);
            }
        }
    }

    public DefaultMQProducerWrapper createProducer(TopicInfo topicInfo) throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        init(defaultMQProducer,topicInfo.getTopicName());
        DefaultMQProducerWrapper defaultMQProducerWrapper = new DefaultMQProducerWrapper(defaultMQProducer,topicInfo.getTopicName());
        activeProducers.add(defaultMQProducerWrapper);
        return defaultMQProducerWrapper;
    }

    private boolean init(DefaultMQProducer defaultMQProducer,String topicName) throws MQClientException {
        defaultMQProducer.setNamesrvAddr(nameServerAddress);
        defaultMQProducer.setProducerGroup(topicName+"-ProducerGroup-"+producerIndex);
        defaultMQProducer.setInstanceName(topicName+"-Producer-"+producerIndex);
        defaultMQProducer.setRetryTimesWhenSendFailed(retyTimesWhenSendFailed);
        defaultMQProducer.start();
        producerIndex++;
        return true;
    }

    @Override
    public List<DefaultMQProducerWrapper> getActiveProducer() {
        return activeProducers;
    }

    @Override
    public List<TopicInfo> getAllTopicInfo() {
        return topicInfos;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public void setRetyTimesWhenSendFailed(int retyTimesWhenSendFailed) {
        this.retyTimesWhenSendFailed = retyTimesWhenSendFailed;
    }

    public void shutDown(){
        for (DefaultMQProducerWrapper activeProducer : activeProducers) {
            activeProducer.getDefaultMQProducer().shutdown();
        }
        activeProducers.clear();
        topicInfos.clear();
    }
}