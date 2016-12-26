package org.bird.breeze.session.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.bird.breeze.session.manager.ISessionManager;

public class ShareHttpRequest extends HttpServletRequestWrapper {

	private ISessionManager sessionManager = null;
	
	public ShareHttpRequest(HttpServletRequest request, ISessionManager sessionManager) {
		super(request);
		this.sessionManager = sessionManager;
		String requestedSessionId = request.getRequestedSessionId();
		if(null != requestedSessionId){
			ShareSession shareSession = sessionManager.getRemoteSession(request.getRequestedSessionId());
			if(null != shareSession){
				shareSession.acesss();
				shareSession.isNew(false);
			}
		}
		
	}

	@Override
	public HttpSession getSession(boolean create) {
		String requestedSessionId = getRequestedSessionId();
		if(null != requestedSessionId){
			ShareSession shareSession = this.sessionManager.getLocalSession(requestedSessionId);
			if(null != shareSession){
				return shareSession;
			}
			if(create){
			shareSession = new ShareSession(getRequestedSessionId(), sessionManager);
				shareSession.acesss();
				sessionManager.setLocalSession(shareSession);
				sessionManager.setRemoteSession(shareSession);
			}
			return shareSession;
		}
		
		return null;
	}
	
	@Override
	public HttpSession getSession() {
		return getSession(false);
	}
	
}
