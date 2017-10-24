package org.bird.breeze.web.member.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.bird.breeze.web.member.util.MemberUtil;
import org.bird.breeze.web.member.util.MenuUtil;





public class MemberServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4353670598791371833L;
	
	private static final Logger log = Logger.getLogger(MemberServlet.class) ;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
//		super.init();
		
//		MemberBean member = new MemberBean();
//		member.setUserName("wangpeng");
//		member.setPassword(DigestUtils.md5Hex("123456"));
//		member.setLastLoginTime(new Timestamp(new Date().getTime()));
//		member.setPasswordUpdatedTime(new Timestamp(new Date().getTime()));
//		
//		MemberUtil.addMember(member);
		
		
		
		MemberUtil.getMembers();
//		System.out.println(MemberUtil.getMember("wangpeng", "e10adc3949ba59abbe56e057f20f883e").getPassword());
		MenuUtil.init();
//		System.out.println(JsonUtil.toJsonString(MenuUtil.getMenus()));
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
	}

}
