package com.channelsoft.ucds.rocket;

import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageMakeCall;
import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.util.rocketmq.RocketMqUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class RocketMqUtilTest {
    @Test
    public void testRocketMQClient() throws Exception{
        for (int i = 0; i < 30; i++) {
            MessageMakeCall messageMakeCall = new MessageMakeCall();
            messageMakeCall.setCaller("caller"+i);
            messageMakeCall.setCalled("called"+i);
            messageMakeCall.setSessionId("session"+i);
            String command = JSONObject.toJSONString(messageMakeCall);
            Boolean isOk = RocketMqUtil.send("messageMakeCall",command.getBytes());
            if (isOk == true) {
                System.out.println("send messageMakeCall success");
            }else {
                System.out.println("send messageMakeCall failed");
            }
            Thread.sleep(3000);
        }
    }

    @Test
    public void testConsumerMessageMakeCall() throws IOException {
        //注册消费者
        RocketMqUtil.registryConsumer("DefaultConsumerGroup","10.130.41.36:9876",
                "messageMakeCall",new MessageListenerConcurrently(){

                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        for (MessageExt msg : msgs) {
                            System.out.println("receive messageMakeCall: " + new String(msg.getBody()));
                            //调用Dubbo进行处理
                            ReplyMakeCall replyMakeCall = new ReplyMakeCall("123123",0);
                            String body = JSONObject.toJSONString(replyMakeCall);
                            boolean isOk = RocketMqUtil.send("replyMakeCall",body.getBytes());
                            if (isOk == true) {
                                System.out.println("send replyMakeCall success");
                            }else{
                                System.out.println("send replyMakeCall failed");
                            }
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                });

        //启动RocketMQ
        RocketMqUtil.startConsumer();

        System.in.read();
    }

    @Test
    public void testConsumerReplyMakeCall() throws IOException {
        //注册消费者
        RocketMqUtil.registryConsumer("DefaultConsumerGroup","10.130.41.36:9876",
                "replyMakeCall",new MessageListenerConcurrently(){
                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        for (MessageExt msg : msgs) {
                            System.out.println("receive " + new String(msg.getBody()));
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                });

        //启动RocketMQ
        RocketMqUtil.startConsumer();

        System.in.read();
    }
}
