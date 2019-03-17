package com.channelsoft.umg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.event.EventAlerting;
import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import com.channelsoft.common.service.ServiceMakeCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public class ServiceMakeCallImpl implements ServiceMakeCall {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMakeCallImpl.class);

    @Override
    public ReplyMakeCall makeCall(String sessionId, String caller, String called, Map<String, Object> extMap) {
        logger.info("umg receive ServiceMakeCall sessionId = {}, caller = {}, called = {}, extMap = {}",sessionId,caller,called,extMap);

        // 注册定时器，发送振铃事件
        TimerTask taskCaller = new TimerTask() {
            @Override
            public void run() {
                EventAlerting eventAlerting = new EventAlerting();
                eventAlerting.setSessionId(sessionId);
                eventAlerting.setSide("caller");
                String event = JSONObject.toJSONString(eventAlerting);
                RocketmqUtil.sendUpStream(sessionId,"eventAlerting",event.getBytes());
                logger.info("umg send event {}",eventAlerting);
            }
        };
        Timer timerCaller = new Timer();
        timerCaller.schedule(taskCaller,2000);

        TimerTask taskCalled = new TimerTask() {
            @Override
            public void run() {
                EventAlerting eventAlerting = new EventAlerting();
                eventAlerting.setSessionId(sessionId);
                eventAlerting.setSide("called");
                String event = JSONObject.toJSONString(eventAlerting);
                RocketmqUtil.sendUpStream(sessionId,"eventAlerting",event.getBytes());
                logger.info("umg send event {}",eventAlerting);
            }
        };
        Timer timerCalled = new Timer();
        timerCalled.schedule(taskCalled,2500);

        //发送replyMakeCall

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("sleep excpetion {}",e);
        }
        ReplyMakeCall replyMakeCall = new ReplyMakeCall(sessionId,0);
        replyMakeCall.setCause("make call normal");
        logger.info("umg reply ReplyMakeCall {}",replyMakeCall);
        return replyMakeCall;
    }
}