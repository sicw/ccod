package com.channelsoft.common.rocketmq.support;

import com.channelsoft.common.rocketmq.DefaultMQProducerWrapper;
import com.channelsoft.common.rocketmq.SelectProducer;

import java.util.List;
import java.util.Random;

/**
 * @author sicwen
 * @date 2019/03/12
 */
public class SimpleSelectProducerImpl implements SelectProducer {

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public DefaultMQProducerWrapper selectOne(List<DefaultMQProducerWrapper> mqs, Object arg) {
        int index = random.nextInt(mqs.size());
        return mqs.get(index);
    }
}