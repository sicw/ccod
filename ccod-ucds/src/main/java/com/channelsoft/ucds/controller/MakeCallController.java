package com.channelsoft.ucds.controller;

import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.message.MessageMakeCall;
import com.channelsoft.common.redis.JedisUtil;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/07
 */
@Controller
public class MakeCallController {

    private final static Logger logger = LoggerFactory.getLogger(MakeCallController.class);

    /**
     * redis集群客户端
     */
    private JedisCluster jc = JedisUtil.getRedisClient();

    @RequestMapping("/makecall")
    public ModelAndView makeCall(String caller, String called, String username){
        /*store msg to redis*/
        Map<String,String> map = new HashMap<>();
        //get enterpriseid from session
        map.put("enterpriseid","null");
        String sessionId = JedisUtil.getGlobalSessioinID();
        map.put("sessionid",sessionId);
        map.put("caller",caller);
        map.put("called",called);
        map.put("username",username);
        //status: new alerting connected drop
        map.put("callerstatus","new");
        map.put("calledstatus","new");
        map.put("starttime",new Long(System.currentTimeMillis()).toString());
        map.put("endtime","null");
        map.put("totaltime","null");
        //record file name: enid_caller_called_date.wav
        map.put("recordfile","enterpriseid_caller_called_date.wav");
        jc.hmset(sessionId,map);

        /*send msg to rocketmq*/
        MessageMakeCall messageMakeCall = new MessageMakeCall();
        messageMakeCall.setCaller(caller);
        messageMakeCall.setCalled(called);
        messageMakeCall.setSessionId(sessionId);
        String command = JSONObject.toJSONString(messageMakeCall);
        RocketmqUtil.send(sessionId,"messageMakeCall",command.getBytes());
        logger.info("send message {}",messageMakeCall);

        ModelAndView mv = new ModelAndView("status");
        mv.getModel().put("username",username);
        mv.getModel().put("sessionId",sessionId);
        //返回状态给坐席端
        return mv;
    }
}