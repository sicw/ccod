package com.channelsoft.ucds.startup;

import com.channelsoft.common.util.rocketmq.RocketMqUtil;
import com.channelsoft.ucds.consumers.ReplyMakeCallMessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/09
 */
@Service
public class InitApp {
    @PostConstruct
    public void init(){
        RocketMqUtil.registryConsumer("DefaultConsumerGroup","10.130.41.36:9876",
                "replyMakeCall",new ReplyMakeCallMessageListenerConcurrently()/*new MessageListenerConcurrently(){
                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        for (MessageExt msg : msgs) {
                            System.out.println("receive " + new String(msg.getBody()));
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                }*/);

        //启动RocketMQ
        RocketMqUtil.startConsumer();
    }
}