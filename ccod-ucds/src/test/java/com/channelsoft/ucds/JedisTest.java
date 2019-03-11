package com.channelsoft.ucds;

import com.channelsoft.common.util.redis.JedisUtil;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author sicwen
 * @date 2019/03/07
 */
public class JedisTest {
    @Test
    public void testJedisSingle() throws IOException {
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        hostAndPorts.add(new HostAndPort("10.130.29.83",6379));
        hostAndPorts.add(new HostAndPort("10.130.29.83",6380));
        hostAndPorts.add(new HostAndPort("10.130.29.83",6381));
        hostAndPorts.add(new HostAndPort("10.130.29.83",6382));
        hostAndPorts.add(new HostAndPort("10.130.29.83",6383));
        hostAndPorts.add(new HostAndPort("10.130.29.83",6384));

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(6);
        jedisPoolConfig.setMaxTotal(12);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setMaxWaitMillis(2000);
        jedisPoolConfig.setTestOnBorrow(true);

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts,jedisPoolConfig);
        jedisCluster.set("name","lily");
        System.out.println(jedisCluster.get("name"));
        jedisCluster.close();
    }

    @Test
    public void testJedisUtil(){
        JedisCluster jc = JedisUtil.getRedisClient();
        jc.set("name","lucy");
        System.out.println(jc.get("name"));
        JedisUtil.closeRedisClient();
    }

    @Test
    public void testJedisHmset(){
        JedisCluster jc = JedisUtil.getRedisClient();
        Map<String,String> map = new HashMap<String,String>();
        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");
        String result = jc.hmset("1001",map);
        System.out.println(result);
        JedisUtil.closeRedisClient();
    }

    @Test
    public void testJedisSessionID(){
        System.out.println(JedisUtil.getGlobalSessioinID());
    }
}
