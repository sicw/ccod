package com.channelsoft.common.service;

import com.channelsoft.common.reply.ReplyRecord;

import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public interface ServiceRecord {
    ReplyRecord record(String sessionId, String side,String fileName, Map<String, Object> extMap);
}
