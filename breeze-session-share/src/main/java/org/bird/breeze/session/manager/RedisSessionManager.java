package org.bird.breeze.session.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.bird.breeze.session.impl.ShareSession;
import org.bird.breeze.util.RedisUtils;
import org.bird.breeze.util.SerializeUtils;
import redis.clients.jedis.Jedis;

public class RedisSessionManager implements ISessionManager {

	private static final long serialVersionUID = -6733912749285642236L;
	private ConcurrentHashMap<String, ShareSession> sessionMap = new ConcurrentHashMap<String, ShareSession>();
	private ServletContext context;
	private int maxInactiveInterval = 20*60*1000;
	
	@Override
	public String[] getSessionIds() {
		if(null != sessionMap){
			ArrayList<String> keys = Collections.list(sessionMap.keys());
			return keys.toArray(new String[0]);
		}
		return null;
	}
	
	@Override
	public ShareSession getRemoteSession(String sessionId) {
		Jedis jedis = RedisUtils.getJedis();
		byte[] keys = SerializeUtils.objectToByte(sessionId);
		byte[] values = jedis.get(keys);
		if(null != values &&values.length > 0) {
			ShareSession session = (ShareSession) SerializeUtils.byteToObject(values);
			RedisUtils.returnResource(jedis);
			return session;
		}
		RedisUtils.returnResource(jedis);
		return null;
	}

	@Override
	public void removeRemoteSession(ShareSession session) {
		Jedis jedis = RedisUtils.getJedis();
		byte[] keys = SerializeUtils.objectToByte(session.getId());
		jedis.del(keys);
		RedisUtils.returnResource(jedis);
	}

	@Override
	public void setRemoteSession(ShareSession session) {
		Jedis jedis = RedisUtils.getJedis();
		byte[] keys = SerializeUtils.objectToByte(session.getId());
		byte[] values = SerializeUtils.objectToByte(session);
		jedis.set(keys,values);
		RedisUtils.returnResource(jedis);
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	@Override
	public ServletContext getServletContext() {
		return this.context;
	}

	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	@Override
	public ShareSession getLocalSession(String sessionId) {
		return sessionMap.get(sessionId);
	}

	@Override
	public void setLocalSession(ShareSession session) {
		if(null != session){
			sessionMap.put(session.getId(), session);
		}
	}
	
	@Override
	public void removeLocalSession(ShareSession session) {
		if(null != session){
			sessionMap.remove(session.getId());
		}
	}

}
