package com.channelsoft.common.rocketmq;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public interface ConsumerFactory {
    /**
     * 创建Consumer
     */
    void refreshConsumers();
}
