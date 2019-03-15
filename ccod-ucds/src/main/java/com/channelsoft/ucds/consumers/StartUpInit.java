package com.channelsoft.ucds.consumers;

import com.channelsoft.common.rocketmq.RocketmqUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author sicwen
 * @date 2019/03/13
 */
public class StartUpInit implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("StartUpInit.afterPropertiesSet");
        RocketmqUtil.createConsumerUpStream(new UcdsMessageListener());
    }
}