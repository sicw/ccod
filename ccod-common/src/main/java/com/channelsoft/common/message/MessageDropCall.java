package com.channelsoft.common.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class MessageDropCall {
    private String sessionId;
    private String side;
    private Map<String,Object> extMap;

    public MessageDropCall(){}

    public void setSide(String side) {
        this.side = side;
    }

    public String getSide() {
        return side;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
}
