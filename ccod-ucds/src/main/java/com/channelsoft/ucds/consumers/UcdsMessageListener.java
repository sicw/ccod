package com.channelsoft.ucds.consumers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.common.event.EventAlerting;
import com.channelsoft.common.event.EventConnected;
import com.channelsoft.common.event.EventDropCall;
import com.channelsoft.common.message.MessageDropCall;
import com.channelsoft.common.message.MessageRecord;
import com.channelsoft.common.redis.JedisUtil;
import com.channelsoft.common.reply.ReplyDropCall;
import com.channelsoft.common.reply.ReplyMakeCall;
import com.channelsoft.common.reply.ReplyRecord;
import com.channelsoft.common.rocketmq.RocketmqUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sicwen
 * @date 2019/03/13
 */
public class UcdsMessageListener implements MessageListenerOrderly {

    private static final Logger logger = LoggerFactory.getLogger(UcdsMessageListener.class);

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            String tag = msg.getTags();
            String messageBody = new String(msg.getBody());
            logger.info("ucds receive a tag {}",tag);
            if ("replyMakeCall".equals(tag)){
                ReplyMakeCall replyMakeCall = null;
                try {
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    replyMakeCall = JSONObject.toJavaObject(jsonObject, ReplyMakeCall.class);
                    logger.info("parse json to replyMakeCall {}",replyMakeCall);
                    String sessionId = replyMakeCall.getSessionId();
                    int causeCode = replyMakeCall.getCauseCode();
                    String cause = replyMakeCall.getCause();
                    if(causeCode == 0){
                        JedisUtil.getRedisClient().hset(sessionId,"callerstatus","calling");
                        JedisUtil.getRedisClient().hset(sessionId,"calledstatus","calling");
                    }else if (causeCode == 10){
                        JedisUtil.getRedisClient().hset(sessionId,"callerstatus","failed");
                        JedisUtil.getRedisClient().hset(sessionId,"calledstatus","calling");
                    }else if (causeCode == 11){
                        JedisUtil.getRedisClient().hset(sessionId,"callerstatus","calling");
                        JedisUtil.getRedisClient().hset(sessionId,"calledstatus","failed");
                    }
                }catch (Exception e){
                    logger.error("ucds receive replyMakeCall exception {}",e);
                }
            }else if ("eventAlerting".equals(tag)){
                try {
                    //更改状态
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    EventAlerting eventAlerting = JSONObject.toJavaObject(jsonObject, EventAlerting.class);
                    String side = eventAlerting.getSide();
                    String sessionId = eventAlerting.getSessionId();
                    JedisUtil.getRedisClient().hset(sessionId, side + "status", "Alerting");

                    //获取录音结果
                    String recordStatus = JedisUtil.getRedisClient().hget(sessionId,"recordstatus");
                    if(!"recording".equals(recordStatus)) {
                        //发送录音命令
                        String caller = JedisUtil.getRedisClient().hget(sessionId, "caller");
                        String called = JedisUtil.getRedisClient().hget(sessionId, "called");
                        MessageRecord record = new MessageRecord();
                        record.setSessionId(sessionId);
                        String time = new SimpleDateFormat("YY-MM-dd HH-mm-ss").format(new Date());
                        record.setRecordFileName(caller + "_" + called + "_" + sessionId + "_" + time);
                        record.setSide("double");
                        String strRecord = JSONObject.toJSONString(record);
                        RocketmqUtil.send(sessionId, "messageRecord", strRecord.getBytes());
                        //更改录音状态
                        JedisUtil.getRedisClient().hset(sessionId, "recordstatus", "recording");
                    }
                }catch (Exception e){
                    logger.error("ucds receive eventAlerting exception {}",e);
                }
            } else if("replyRecord".equals(tag)){
                //更改录音状态
                JSONObject jsonObject = JSON.parseObject(messageBody);
                ReplyRecord replyRecord = JSONObject.toJavaObject(jsonObject,ReplyRecord.class);
                String sessionId = replyRecord.getSessionId();
                JedisUtil.getRedisClient().hset(sessionId,"recordstatus",replyRecord.getCause());
            } else if("eventConnected".equals(tag)){
                //更改录音状态
                JSONObject jsonObject = JSON.parseObject(messageBody);
                EventConnected eventConnected = JSONObject.toJavaObject(jsonObject,EventConnected.class);
                String sessionId = eventConnected.getSessionId();
                JedisUtil.getRedisClient().hset(sessionId,"callerstatus","connected");
                JedisUtil.getRedisClient().hset(sessionId,"calledstatus","connected");
            } else if("eventDropCall".equals(tag)){
                try {
                    //更改状态
                    JSONObject jsonObject = JSON.parseObject(messageBody);
                    EventDropCall eventDropCall = JSONObject.toJavaObject(jsonObject, EventDropCall.class);
                    String sessionId = eventDropCall.getSessionId();
                    String side = eventDropCall.getSide();
                    String cause = eventDropCall.getCause();
                    if ("called".equals(side)) {
                        JedisUtil.getRedisClient().hset(sessionId, "calledstatus", "dropcall");
                    } else if ("caller".equals(side)) {
                        JedisUtil.getRedisClient().hset(sessionId, "callerstatus", "dropcall");
                        JedisUtil.getRedisClient().hset(sessionId, "endtime", new Long(System.currentTimeMillis()).toString());
                    }

                    //调用DropCall
                    MessageDropCall dropCall = new MessageDropCall();
                    dropCall.setSessionId(sessionId);
                    dropCall.setSide("caller");
                    String json = JSONObject.toJSONString(dropCall);
                    RocketmqUtil.send(sessionId,"messageDropCall",json.getBytes());
                    logger.info("ucds send message {}",dropCall);

                    String callerStatus = JedisUtil.getRedisClient().hget(sessionId, "callerstatus");
                    String calledStatus = JedisUtil.getRedisClient().hget(sessionId, "calledstatus");
                    if ("dropcall".equals(callerStatus) && "dropcall".equals(calledStatus)) {
                        //清除缓存
                        //发送呼叫明细
                        Map<String, String> allInfo = JedisUtil.getRedisClient().hgetAll(sessionId);
                        logger.info("ucds get all info {}", allInfo);
                    }
                }catch (Exception e){
                    logger.error("ucds receive dropCall exception {}",e);
                }
            }else if ("replyDropCall".equals(tag)){
                JSONObject jsonObject = JSON.parseObject(messageBody);
                ReplyDropCall replyDropCall = JSONObject.toJavaObject(jsonObject, ReplyDropCall.class);
                String sessionId = replyDropCall.getSessionId();
                String side = replyDropCall.getSide();
                String cause = replyDropCall.getCause();
                if ("called".equals(side)) {
                    JedisUtil.getRedisClient().hset(sessionId, "calledstatus", "dropcall");
                } else if ("caller".equals(side)) {
                    JedisUtil.getRedisClient().hset(sessionId, "callerstatus", "dropcall");
                    JedisUtil.getRedisClient().hset(sessionId, "endtime", new Long(System.currentTimeMillis()).toString());
                }
                String callerStatus = JedisUtil.getRedisClient().hget(sessionId, "callerstatus");
                String calledStatus = JedisUtil.getRedisClient().hget(sessionId, "calledstatus");
                if ("dropcall".equals(callerStatus) && "dropcall".equals(calledStatus)) {
                    //清除缓存
                    //发送呼叫明细
                    Map<String, String> allInfo = JedisUtil.getRedisClient().hgetAll(sessionId);
                    logger.info("ucds get all info {}", allInfo);
                }
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}