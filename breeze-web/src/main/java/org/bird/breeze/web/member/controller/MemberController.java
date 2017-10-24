package org.bird.breeze.web.member.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.bird.breeze.web.base.util.HttpUtil;
import org.bird.breeze.web.member.bean.MemberBean;
import org.bird.breeze.web.member.constant.MemberConstant;
import org.bird.breeze.web.member.util.MemberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/breeze/member")
public class MemberController {
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response){
		
		return "login";
	}
	
	@RequestMapping("/memberLogin")
	public String memberLogin(String userName, String password, HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String errMsg = StringUtils.EMPTY;
		if(StringUtils.isEmpty(userName)){
			errMsg = "请输入用户名";
		}
		if(StringUtils.isEmpty(password)){
			errMsg = "请输入密码";
		}
		String passMd5 = DigestUtils.md5Hex(password);
		MemberBean member = MemberUtil.getMember(userName, passMd5);
		if(null == member){
			errMsg = "用户名或密码不正确";
		}
		
		if(StringUtils.isEmpty(errMsg)){
			HttpSession session = request.getSession(true);
			session.setAttribute(MemberConstant.MEM_INFO, member);
			returnMap.put("isSuccess", true);
			returnMap.put("errMsg", null);
		}
		else{
			returnMap.put("isSuccess", false);
			returnMap.put("errMsg", errMsg);
		}
		
		return HttpUtil.returnJson(response, returnMap);
	}
	
	@RequestMapping("/memberLogout")
	public String memberLogout(HttpServletRequest request, HttpServletResponse response){
		
		HttpSession session = request.getSession();
		session.removeAttribute(MemberConstant.MEM_INFO);
		session.invalidate();
		request.setAttribute(MemberConstant.LOGOUT_FLAG, true);
		
		return "login";
	}
	
}
