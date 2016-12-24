package org.bird.breeze.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	private static JedisPool jedisPool = null;
	private static InputStream is ;
	private static Properties properties = new Properties() ;
	private static Logger logger = Logger.getLogger(RedisUtils.class);
	
	/**
	* 初始化Redis连接池
	*/
	static {
		try {
			is = RedisUtils.class.getClassLoader().getResourceAsStream("breeze-config.properties") ;
			if(is!=null)
				properties.load(is) ;
			JedisPoolConfig config = new JedisPoolConfig();
			//可用连接实例的最大数目，默认值为8；
			config.setMaxTotal(Integer.parseInt((String)properties.get("redis.maxactive")));
			//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
			config.setMaxIdle(Integer.parseInt((String)properties.get("redis.maxIdle")));
			config.setMaxWaitMillis(Integer.parseInt((String)properties.get("redis.timeout")));
			jedisPool = new JedisPool(config, (String)properties.get("redis.ip"), 
					Integer.parseInt((String)properties.get("redis.port")), 
					Integer.parseInt((String)properties.get("redis.timeout")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                logger.debug("Getting a redis resource success");
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
        	jedis.close();
        	logger.debug("Closing a redis resource success");
        }
    }
	
}
