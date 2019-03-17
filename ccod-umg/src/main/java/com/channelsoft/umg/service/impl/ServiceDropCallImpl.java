package com.channelsoft.umg.service.impl;

import com.channelsoft.common.reply.ReplyDropCall;
import com.channelsoft.common.service.ServiceDropCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/17
 */
public class ServiceDropCallImpl implements ServiceDropCall {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDropCallImpl.class);

    @Override
    public ReplyDropCall dropCall(String sessionId, String side, Map<String, Object> extMap) {
        logger.info("umg receice dropCall sessionId={},side={},extMap={}",sessionId,side,extMap);
        ReplyDropCall dropCall = null;
        try {
            Thread.sleep(1500);
            dropCall = new ReplyDropCall();
            dropCall.setSessionId(sessionId);
            dropCall.setCause("normal drop call");
            dropCall.setSide("caller");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("umg reply {}",dropCall);
        return dropCall;
    }
}
