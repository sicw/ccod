package com.channelsoft.cms.startup;

import com.channelsoft.common.util.rocketmq.RocketMqUtil;
import com.channelsoft.cms.consumers.message.MakeCallMessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
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

        //注册消费者
        MessageListenerConcurrently messageListener = context.getBean(MakeCallMessageListenerConcurrently.class);
        RocketMqUtil.registryConsumer("DefaultConsumerGroup","10.130.41.36:9876",
                "messageMakeCall", messageListener);

//        RocketMqUtil.registryConsumer("DefaultConsumerGroup","10.130.41.36:9876",
//                "messageMakeCall", new MakeCallMessageListenerConcurrently());

        //启动RocketMQ
        RocketMqUtil.startConsumer();

        synchronized (App.class){
            App.class.wait();
        }
    }
}