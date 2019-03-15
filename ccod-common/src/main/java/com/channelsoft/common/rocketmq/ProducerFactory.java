package com.channelsoft.common.rocketmq;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public interface ProducerFactory {
    /**
     * 更新TopicInfo，创建Producer
     */
    void refreshProducers();

    /**
     * 返回可用的Producers
     * @return
     */
    List<DefaultMQProducerWrapper> getActiveProducer();

    /**
     * 获取所有Topic信息
     * @return
     */
    List<TopicInfo> getAllTopicInfo();
}