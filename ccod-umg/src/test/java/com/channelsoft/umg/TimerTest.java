package com.channelsoft.umg;

import org.junit.Test;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sicwen
 * @date 2019/03/16
 */
public class TimerTest {
    @Test
    public void testTimer() throws IOException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer...");
            }
        };
        Timer timer = new Timer();
        long delay = 1000;
        long intervalPeriod = 1 * 1000;
        //定时delay后执行一次
        timer.schedule(task,delay);
        //定时delay后开始执行，intervalPeriod周期的执行
        //timer.schedule(task,delay,intervalPeriod);

        System.in.read();
    }
}
