package com.channelsoft.cms.startup;

import com.channelsoft.cms.consumers.message.CmsMessageListener;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        //启动Dubbo
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        //调用实现了LifeBean接口的bean
        context.start();
        RocketmqUtil.createConsumer(context.getBean(CmsMessageListener.class));
        synchronized (App.class){
            App.class.wait();
        }
    }
}