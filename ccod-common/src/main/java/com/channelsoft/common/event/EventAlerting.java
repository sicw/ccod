package com.channelsoft.common.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class EventAlerting {
    private String sessionId;
    private String side;
    private Map<String,Object> extMap;

    public EventAlerting() {
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
        return "EventAlerting{" +
                "sessionId='" + sessionId + '\'' +
                ", side='" + side + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}
