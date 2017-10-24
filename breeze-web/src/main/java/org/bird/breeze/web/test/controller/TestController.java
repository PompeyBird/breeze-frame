package org.bird.breeze.web.test.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/testInter")
	public String testInter(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String str = "{\"RESPONSE_TIME\":\"20170818102033\",\"TRADE_TYPE\":1,\"CONTENT\":{\"RESULT_CODE\":\"1000\",\"RESULT_DESC\":\"aaa\"}}";
		response.getWriter().write(str);
		return null;
	}
	
}
