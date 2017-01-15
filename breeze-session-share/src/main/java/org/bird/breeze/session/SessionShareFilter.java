package org.bird.breeze.session;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bird.breeze.session.impl.ShareHttpRequest;
import org.bird.breeze.session.impl.ShareSession;
import org.bird.breeze.session.manager.ISessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SessionShareFilter implements Filter {
	
	private static Logger logger = Logger.getLogger(SessionShareFilter.class);
	
	private ISessionManager sessionMgr;

	@Override
	public void destroy() {
		sessionMgr = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpSession session = request.getSession(true);
		sessionMgr.setMaxInactiveInterval(session.getMaxInactiveInterval());
		ShareHttpRequest shareRequest = new ShareHttpRequest(request, sessionMgr);
		session.invalidate();
		chain.doFilter(shareRequest, resp);
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		String sessionClientImplId = fc.getInitParameter("session_manager_id");
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(fc.getServletContext());
    	sessionMgr = ((ISessionManager)ctx.getBean(sessionClientImplId));
    	sessionMgr.setServletContext(fc.getServletContext());
    	
    	Runnable runnable = new Runnable(){

			@Override
			public void run() {
				String[] sessionIds = sessionMgr.getSessionIds();
				for(String sessionId : sessionIds){
					ShareSession session = sessionMgr.getLocalSession(sessionId);
					if(session.getMaxInactiveInterval() + session.getLastAccessedTime() < System.currentTimeMillis()){
						sessionMgr.removeLocalSession(session);
						logger.debug("remove session from local");
						sessionMgr.removeRemoteSession(session);
						logger.debug("remove session from remote");
					}
				}
			}
			
		};
		ScheduledExecutorService sessionService = Executors.newSingleThreadScheduledExecutor();
		sessionService.scheduleAtFixedRate(runnable, 0, 120L, TimeUnit.SECONDS);
	}

}
