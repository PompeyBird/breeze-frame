package org.bird.breeze.session.manager;

import javax.servlet.ServletContext;

import org.bird.breeze.session.impl.ShareSession;

public interface ISessionManager {
	
	public ShareSession getRemoteSession(String seesionId);
	public void removeRemoteSession(ShareSession seesion);
	public void setRemoteSession(ShareSession seesion);
	public ShareSession getLocalSession(String seesionId);
	public void setLocalSession(ShareSession seesion);
	public int getMaxInactiveInterval();
	public void setMaxInactiveInterval(int maxInactiveInterval);
	public ServletContext getServletContext();
	public void setServletContext(ServletContext context);
	
}
