package org.bird.breeze.web.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bird.breeze.web.test.bean.Country;
import org.bird.breeze.web.test.service.ICountrySvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class CountryController {
	
	@Autowired
	private ICountrySvc iCountrySvc;
	
	@RequestMapping("/queryContryDetail")
	public String queryContryDetail(HttpServletRequest request, HttpServletResponse response){
		
		HttpSession session = request.getSession();
		session.setAttribute("test", "test-spring-session");
		
		System.out.println(session.getId());
		
		Country country = iCountrySvc.getCountry("ABW");
		
		return "/pages/test/test";
	}
	
}
