package org.bird.breeze.session.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.bird.breeze.session.manager.ISessionManager;

@SuppressWarnings("deprecation")
public class ShareSession implements HttpSession  {
	
	private String sessionId = null;
	private ConcurrentHashMap<String, Object> attributes = null;
	private ISessionManager sessionManager = null;
	private boolean isnew = false;
	private long creationTime = 0;
	private long lastAccessedTime = 0;
	
	public ShareSession(String sessionId, ISessionManager sessionManager){
		this.sessionId = sessionId;
		this.isnew = true;
		this.attributes = new ConcurrentHashMap<String, Object>();
		this.sessionManager = sessionManager;
		long currentTime = System.currentTimeMillis();
		this.creationTime = currentTime;
		this.lastAccessedTime = currentTime;
	}

	@Override
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return attributes.keys();
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public String getId() {
		return sessionId;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.sessionManager.getMaxInactiveInterval();
	}

	@Override
	public ServletContext getServletContext() {
		return this.sessionManager.getServletContext();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public Object getValue(String key) {
		return getAttribute(key);
	}

	@Override
	@Deprecated
	public String[] getValueNames() {
		ArrayList<String> list = Collections.list(getAttributeNames());
		String[] names = new String[]{};
		names = list.toArray(names);
		return names;
	}

	@Override
	public void invalidate() {
		if(null != sessionManager){
			sessionManager.removeLocalSession(this);
			sessionManager.removeLocalSession(this);
		}
	}

	@Override
	public boolean isNew() {
		return isnew;
	}

	@Override
	@Deprecated
	public void putValue(String key, Object value) {
		setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		this.attributes.remove(key);
	}

	@Override
	@Deprecated
	public void removeValue(String key) {
		removeAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.sessionManager.setMaxInactiveInterval(maxInactiveInterval);
	}
	
	public void acesss(){
		this.lastAccessedTime = System.currentTimeMillis();
	}

	public void isNew(boolean flag){
		this.isnew = flag;
	}
}
