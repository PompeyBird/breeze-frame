package org.bird.breeze.session.manager;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.bird.breeze.session.impl.ShareSession;

public class RedisSessionManager implements ISessionManager {

	private ConcurrentHashMap<String, ShareSession> sessionMap = new ConcurrentHashMap<String, ShareSession>();
	private ServletContext context;
	private int maxInactiveInterval = 20*60*1000;
	
	@Override
	public ShareSession getRemoteSession(String seesionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRemoteSession(ShareSession seesion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRemoteSession(ShareSession seesion) {
		// TODO Auto-generated method stub
		
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
	public ShareSession getLocalSession(String seesionId) {
		return sessionMap.get(seesionId);
	}

	@Override
	public void setLocalSession(ShareSession seesion) {
		if(null != seesion){
			sessionMap.put(seesion.getId(), seesion);
		}
	}

}
