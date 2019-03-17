package com.channelsoft.umg;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author sicwen
 * @date 2019/03/16
 */
public class QuartzTest {
    @Test
    public void testCase() throws SchedulerException, InterruptedException {
        //创建调度器
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        //创建job实例，与PrintJob绑定
        JobDetail jobDetail = JobBuilder.newJob(PrintJob.class).withIdentity("job1","group1").build();

        //构建触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .startNow()//立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)//每隔1s执行一次
                        .repeatForever()).build();//一直执行

        //执行
        scheduler.scheduleJob(jobDetail,trigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();

        //睡眠
        TimeUnit.MINUTES.sleep(1);
        scheduler.shutdown();
        System.out.println("--------scheduler shutdown ! ------------");
    }
}