package com.channelsoft.ucds.controller;

import com.channelsoft.common.redis.JedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sicwen
 * @date 2019/03/14
 */
@Controller
public class AjaxController {

    private static int count = 1;

    @RequestMapping("/ajax")
    @ResponseBody
    public AjaxResponse ajaxReuqest(String username, String sessionId, Model model){
        //根据sesisonId去redis中查找数据
        String calledstatus = JedisUtil.getRedisClient().hget(sessionId,"calledstatus");
        String callerstatus = JedisUtil.getRedisClient().hget(sessionId,"callerstatus");
        model.addAttribute("sessionId",sessionId);
        //封装成对象返回
        return new AjaxResponse(username,sessionId,callerstatus,calledstatus);
    }

    private class AjaxResponse{
        private String username;
        private String sessionId;
        public String callerstatus;
        public String calledstatus;

        public AjaxResponse(String username,String sessionId,String callerstatus, String calledstatus) {
            this.username = username;
            this.sessionId = sessionId;
            this.callerstatus = callerstatus;
            this.calledstatus = calledstatus;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public String getCallerstatus() {
            return callerstatus;
        }

        public void setCallerstatus(String callerstatus) {
            this.callerstatus = callerstatus;
        }

        public String getCalledstatus() {
            return calledstatus;
        }

        public void setCalledstatus(String calledstatus) {
            this.calledstatus = calledstatus;
        }
    }
}