package com.channelsoft.umg;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sicwen
 * @date 2019/03/16
 */
public class PrintJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String currentTime = new SimpleDateFormat("YY-MM-HH-mm-ss").format(new Date());
        System.out.println(currentTime);
    }
}
