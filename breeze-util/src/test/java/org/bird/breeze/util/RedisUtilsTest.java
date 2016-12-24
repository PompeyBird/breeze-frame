package org.bird.breeze.util;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisUtilsTest {

	@Test
	public void testRedisUtils(){
		Jedis jedis = RedisUtils.getJedis();
		jedis.setnx("test", "Hello World");
		System.out.println("set redis value OK");
		String value = jedis.get("test");
		System.out.println("get redis value: "+value);
		RedisUtils.returnResource(jedis);
	}
	
}
