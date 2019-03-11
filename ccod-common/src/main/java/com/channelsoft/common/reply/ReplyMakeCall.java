package com.channelsoft.common.reply;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * causeCode:
 *      0: 呼叫双方成功
 *      10：呼叫坐席失败
 *      11：呼叫用户失败
 * cause:
 *      呼叫失败原因值
 * @author sicwen
 * @date 2019/03/09
 */
public class ReplyMakeCall implements Serializable {

    private String sessionId;
    private int causeCode;
    private String cause;

    private Map<String,Object> extMap;

    public ReplyMakeCall(){

    }

    public ReplyMakeCall(String sessionId, int causeCode){
        this.sessionId = sessionId;
        this.causeCode = causeCode;
        this.cause = "";
    }

    public ReplyMakeCall(String sessionId, int causeCode, String cause){
        this.sessionId = sessionId;
        this.causeCode = causeCode;
        this.cause = cause;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(int causeCode) {
        this.causeCode = causeCode;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setAttribute(String key, Object obj) {
        if (extMap == null) {
            extMap = new HashMap<String,Object>();
        }
        extMap.put(key, obj);
    }

    public Object getAttribute(String key){
        return extMap.get(key);
    }

    @Override
    public String toString() {
        return "ReplyMakeCall{" +
                "sessionId='" + sessionId + '\'' +
                ", causeCode=" + causeCode +
                ", cause='" + cause + '\'' +
                '}';
    }
}
