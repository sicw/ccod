package com.channelsoft.ucds.rocket;

import com.channelsoft.common.util.rocketmq.RocketMqUtil;
import org.junit.Test;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class MqSelectorTest {
    @Test
    public void testMessageQueueSelector() throws InterruptedException {
        for (int i = 1; i <= 30; i++) {
            Boolean isOk = RocketMqUtil.send("TopicSelector",("TopicSelector"+i).getBytes(),new MessageQueueSelectorWithSessionId(),"00"+i);
            if (isOk == true) {
                System.out.println("send message"+i+" success");
            }else {
                System.out.println("send message"+i+" failed");
            }
            Thread.sleep(1000);

        }
    }
}
