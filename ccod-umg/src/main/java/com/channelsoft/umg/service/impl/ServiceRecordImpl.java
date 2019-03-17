package com.channelsoft.umg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.event.EventConnected;
import com.channelsoft.common.event.EventDropCall;
import com.channelsoft.common.reply.ReplyRecord;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import com.channelsoft.common.service.ServiceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sicwen
 * @date 2019/03/16
 */
public class ServiceRecordImpl implements ServiceRecord {

    private final Logger logger = LoggerFactory.getLogger(ServiceRecordImpl.class);

    @Override
    public ReplyRecord record(String sessionId, String side,String fileName, Map<String, Object> extMap) {
        logger.info("umg receive MessageRecord sessionId={},side={},fileName={},extMap={}",sessionId,side,fileName,extMap);

        //设置connected事件
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                EventConnected eventConnected = new EventConnected();
                eventConnected.setSessionId(sessionId);
                String json = JSONObject.toJSONString(eventConnected);
                RocketmqUtil.sendUpStream(sessionId,"eventConnected",json.getBytes());
                logger.info("umg send event {}",eventConnected);
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,2000);

        //设置外线挂断事件
        TimerTask dropTask = new TimerTask() {
            @Override
            public void run() {
                EventDropCall eventDropCall = new EventDropCall();
                eventDropCall.setSessionId(sessionId);
                eventDropCall.setSide("called");
                eventDropCall.setCause("normal drop");
                eventDropCall.setCauseCode(0);
                String json = JSONObject.toJSONString(eventDropCall);
                RocketmqUtil.sendUpStream(sessionId,"eventDropCall",json.getBytes());
                logger.info("umg send event {}",eventDropCall);
            }
        };
        Timer calledTimer = new Timer();
        calledTimer.schedule(dropTask,6000);

        //返回ReplyRecord
        ReplyRecord replyRecord = new ReplyRecord();
        try {
            Thread.sleep(1000);
            replyRecord.setSessionId(sessionId);
            replyRecord.setCauseCode(0);
            replyRecord.setCause("double side record sucess");
            replyRecord.setSide("double");
        } catch (InterruptedException e) {
            logger.error("");
        }
        logger.info("umg reply {}",replyRecord);
        return replyRecord;
    }
}