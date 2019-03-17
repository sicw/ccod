package com.channelsoft.common.reply;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class ReplyDropCall implements Serializable {
    private String sessionId;
    private String side;
    private int causeCode;
    private String cause;
    private Map<String,Object> extMap;

    public ReplyDropCall() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
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
        return "ReplyDropCall{" +
                "sessionId='" + sessionId + '\'' +
                ", side='" + side + '\'' +
                ", causeCode=" + causeCode +
                ", cause='" + cause + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}
