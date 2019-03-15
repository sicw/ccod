package com.channelsoft.umg.service.impl;

import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.service.ServiceMakeCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public class ServiceMakeCallImpl implements ServiceMakeCall {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMakeCallImpl.class);

    @Override
    public ReplyMakeCall makeCall(String sessionId, String caller, String called, Map<String, Object> extMap) {
        logger.info("umg receive ServiceMakeCall sessionId = {}, caller = {}, called = {}, extMap = {}",sessionId,caller,called,extMap);
        ReplyMakeCall replyMakeCall = new ReplyMakeCall(sessionId,0);
        replyMakeCall.setCause("make call normal");
        logger.info("umg reply ReplyMakeCall {}",replyMakeCall);
        return replyMakeCall;
    }
}