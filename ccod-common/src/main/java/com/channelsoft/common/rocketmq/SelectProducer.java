package com.channelsoft.common.rocketmq;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public interface SelectProducer {
    /**
     * 使用arg，根据一定规则选择一个Producer
     * @param mqs 可用Producer
     * @param arg 选择条件
     * @return
     */
    DefaultMQProducerWrapper selectOne(List<DefaultMQProducerWrapper> mqs,Object arg);
}
