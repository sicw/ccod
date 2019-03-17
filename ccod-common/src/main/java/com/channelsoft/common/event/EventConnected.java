package com.channelsoft.common.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/11
 */
public class EventConnected {
    private String sessionId;
    private Map<String,Object> extMap;

    public EventConnected() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
        return "EventConnected{" +
                "sessionId='" + sessionId + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}
