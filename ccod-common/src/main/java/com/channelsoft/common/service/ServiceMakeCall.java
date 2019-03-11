package com.channelsoft.common.service;

import com.channelsoft.common.reply.ReplyMakeCall;

import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public interface ServiceMakeCall {
    /**
     * 返回状态结果
     * 0：呼叫双方成功
     * 10：呼叫坐席失败
     * 11：呼叫用户失败
     * @param sessionId
     * @param caller
     * @param called
     * @param extMap
     * @return
     */
    ReplyMakeCall makeCall(String sessionId, String caller, String called, Map<String, Object> extMap);
}