package com.channelsoft.common.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class EventDropCall {
    private String sessionId;
    private String side;
    private int causeCode;
    private String cause;
    private Map<String,Object> extMap;

    public EventDropCall() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setCauseCode(int causeCode) {
        this.causeCode = causeCode;
    }

    public int getCauseCode() {
        return causeCode;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
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
        return "EventDropCall{" +
                "sessionId='" + sessionId + '\'' +
                ", side='" + side + '\'' +
                ", causeCode=" + causeCode +
                ", cause='" + cause + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}
