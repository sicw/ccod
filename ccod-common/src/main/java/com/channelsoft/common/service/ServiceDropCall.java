package com.channelsoft.common.service;

import com.channelsoft.common.reply.ReplyDropCall;

import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public interface ServiceDropCall {
    ReplyDropCall dropCall(String sessionId, String side, Map<String,Object> extMap);
}
