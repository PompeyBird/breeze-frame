package org.bird.breeze.session;


import org.bird.breeze.session.impl.ShareSession;
import org.bird.breeze.session.manager.ISessionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SessionShareTest {

    private ISessionManager sessionManager;

    @Before
	public void init(){

        ApplicationContext ctx =  new ClassPathXmlApplicationContext("applicationContext.xml");
        sessionManager = (ISessionManager) ctx.getBean("session_manager_id");

    }

    @Test
    public void testSetRemoteSession(){
        sessionManager.setMaxInactiveInterval(20*60);
        String sessionId = "dsadadasdsadadweqwdsada";
        ShareSession session = new ShareSession(sessionId, sessionManager);
        sessionManager.setRemoteSession(session);
    }

    @Test
    public void testGetRemoteSession(){
        String sessionId = "dsadadasdsadadweqwdsada";
        ShareSession session = sessionManager.getRemoteSession(sessionId);
        System.out.println(session.getId());
    }

    @Test
    public void testDelRemoteSession(){
        String sessionId = "dsadadasdsadadweqwdsada";
        ShareSession session = sessionManager.getRemoteSession(sessionId);
        sessionManager.removeRemoteSession(session);
        System.out.println(session.getId());
    }

}
