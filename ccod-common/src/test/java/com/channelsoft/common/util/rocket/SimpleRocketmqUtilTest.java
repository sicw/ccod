package com.channelsoft.common.util.rocket;

import com.channelsoft.common.rocketmq.RocketmqUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/13
 */
public class SimpleRocketmqUtilTest {
    @Test
    public void producer(){
        for (int i = 0; i < 5; i++) {
            RocketmqUtil.send("1123","messageMakeCall","this is messageMakeCall".getBytes());
        }
    }

    @Test
    public void consumer() throws IOException {
        RocketmqUtil.createConsumer(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                String tag = msgs.get(0).getTags();
                if (tag.equals("messageMakeCall")){
                    System.out.println(new String(msgs.get(0).getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        System.in.read();
    }

    @Test
    public void producerUpStream(){
        for (int i = 0; i < 5; i++) {
            RocketmqUtil.sendUpStream("1123","replyMakeCall","this is replyMakeCall".getBytes());
        }
    }

    @Test
    public void consumerUpStream() throws IOException {
        RocketmqUtil.createConsumerUpStream(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                String tag = msgs.get(0).getTags();
                if (tag.equals("replyMakeCall")){
                    System.out.println(new String(msgs.get(0).getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        System.in.read();
    }
}