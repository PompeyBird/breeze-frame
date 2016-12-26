package org.bird.breeze.session.manager;

import javax.servlet.ServletContext;

import org.bird.breeze.session.impl.ShareSession;

public interface ISessionManager {
	
	public String[] getSessionIds();
	public ShareSession getRemoteSession(String sessionId);
	public void removeRemoteSession(ShareSession session);
	public void setRemoteSession(ShareSession session);
	public ShareSession getLocalSession(String sessionId);
	public void setLocalSession(ShareSession session);
	public void removeLocalSession(ShareSession session);
	public int getMaxInactiveInterval();
	public void setMaxInactiveInterval(int maxInactiveInterval);
	public ServletContext getServletContext();
	public void setServletContext(ServletContext context);
	
}
