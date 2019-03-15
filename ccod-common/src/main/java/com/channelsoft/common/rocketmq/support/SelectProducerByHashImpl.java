package com.channelsoft.common.rocketmq.support;

import com.channelsoft.common.rocketmq.DefaultMQProducerWrapper;
import com.channelsoft.common.rocketmq.SelectProducer;

import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class SelectProducerByHashImpl implements SelectProducer {
    @Override
    public DefaultMQProducerWrapper selectOne(List<DefaultMQProducerWrapper> mqs, Object arg) {
        int value = arg.hashCode();
        if (value < 0) {
            value = Math.abs(value);
        }
        value = value % mqs.size();
        return mqs.get(value);
    }
}