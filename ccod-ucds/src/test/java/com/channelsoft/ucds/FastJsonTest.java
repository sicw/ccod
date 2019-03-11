package com.channelsoft.ucds;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageMakeCall;
import org.junit.Test;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class FastJsonTest {

    @Test
    public void testObject2Json(){
        MessageMakeCall messageMakeCall = new MessageMakeCall();
        messageMakeCall.setCalled("123");
        messageMakeCall.setCaller("11");
        messageMakeCall.setSessionId("11111");
        messageMakeCall.setAttribute("name","sicwen");
        String json = JSONObject.toJSONString(messageMakeCall);
        System.out.println(json);
    }

    @Test
    public void testJson2Object(){
        String json = "{\"called\":\"123\",\"caller\":\"11\",\"extMap\":{\"name\":\"sicwen\"},\"sessionId\":\"11111\"}";
        JSONObject jsonObject = JSON.parseObject(json);
        MessageMakeCall messageMakeCall = JSONObject.toJavaObject(jsonObject, MessageMakeCall.class);
        System.out.println(messageMakeCall.getCalled());
        System.out.println(messageMakeCall.getCaller());
        System.out.println(messageMakeCall.getSessionId());
        System.out.println(messageMakeCall.getAttribute("name"));
    }
}
