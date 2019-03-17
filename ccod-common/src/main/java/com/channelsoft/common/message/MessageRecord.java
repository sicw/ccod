package com.channelsoft.common.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class MessageRecord {
    private String sessionId;
    private String recordFileName;
    private String side;

    private Map<String,Object> extMap;

    public MessageRecord(){}

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSide() {
        return side;
    }

    public void setRecordFileName(String recordFileName) {
        this.recordFileName = recordFileName;
    }

    public String getRecordFileName() {
        return recordFileName;
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
        return "MessageRecord{" +
                "sessionId='" + sessionId + '\'' +
                ", recordFileName='" + recordFileName + '\'' +
                ", side='" + side + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}
