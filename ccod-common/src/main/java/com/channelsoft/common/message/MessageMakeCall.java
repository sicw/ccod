package com.channelsoft.common.message;

import java.util.HashMap;
import java.util.Map;

/**
 * ucds:  MessageMakeCall                                  MessageRecord                               MessageDropCall
 * umg:                    ReplyMakeCall    EventAlerting                 ReplyRecord  EventConnected                   ReplyDropCall  EventDropCall
 *
 *
 * @author sicwen
 * @date 2019/03/07
 */
public class MessageMakeCall {
    private String caller;
    private String called;
    private String sessionId;
    private Map<String,Object> extMap;

    public MessageMakeCall(){}

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionid) {
        this.sessionId = sessionid;
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
        return "MessageMakeCall{" +
                "caller='" + caller + '\'' +
                ", called='" + called + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", extMap=" + extMap +
                '}';
    }
}
