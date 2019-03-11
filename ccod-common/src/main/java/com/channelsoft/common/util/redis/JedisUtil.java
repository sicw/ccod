package com.channelsoft.common.util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class JedisUtil {

    private final static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    private static JedisCluster jedisCluster;

    /**
     * 提前连接失败，防止上线运行后报错
     */
    static{
        try {
            Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
            hostAndPorts.add(new HostAndPort("10.130.29.83", 6379));
            hostAndPorts.add(new HostAndPort("10.130.29.83", 6380));
            hostAndPorts.add(new HostAndPort("10.130.29.83", 6381));
            hostAndPorts.add(new HostAndPort("10.130.29.83", 6382));
            hostAndPorts.add(new HostAndPort("10.130.29.83", 6383));
            hostAndPorts.add(new HostAndPort("10.130.29.83", 6384));

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(6);
            jedisPoolConfig.setMaxTotal(12);
            jedisPoolConfig.setMinIdle(6);
            jedisPoolConfig.setMaxWaitMillis(2000);
            jedisPoolConfig.setTestOnBorrow(true);
            jedisCluster = new JedisCluster(hostAndPorts, jedisPoolConfig);
        }catch (Exception e){
            if (jedisCluster != null) {
                try {
                    jedisCluster.close();
                } catch (IOException e1) {
                    logger.error("close redis cluster error: {}",e1);
                }
            }
            logger.error("connection redis cluster error: {}",e);
        }
    }

    public static JedisCluster getRedisClient(){
        return jedisCluster;
    }

    public static void closeRedisClient(){
        try {
            if (jedisCluster != null) {
                jedisCluster.close();
                jedisCluster = null;
            }
        } catch (IOException e) {
            logger.error("close redis cluster error {}",e);
        }
    }

    /**
     * 获取分布式session id
     * @return
     */
    public static String getGlobalSessioinID(){
        Long sessionID =  jedisCluster.incr("SESSION_ID");
        String strSessionID = String.format("%016d",sessionID);
        return strSessionID;
    }
}