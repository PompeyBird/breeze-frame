package org.bird.breeze.web.test;

import org.bird.breeze.web.test.bean.Country;
import org.bird.breeze.web.test.service.ICountrySvc;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

public class ICountrySvcTest {

	private ICountrySvc svc;
	
	@SuppressWarnings("resource")
	@Before
	public void getService(){
		ApplicationContext ctx =  new ClassPathXmlApplicationContext("applicationContext.xml");
        svc = ctx.getBean(ICountrySvc.class);
	}
	
	@Test
	public void testQueryContryList(){
		Country country = new Country();
		
//		List<Country> list = svc.queryContryList(country, 1, 10);
		PageInfo<Country> page = svc.queryContryList(country, 1, 10);
		
		System.out.println(JSONObject.toJSON(page));
	}
	
	@Test
	public void testGetCountry(){
		String code = "ABW";
		
		Country country = svc.getCountry(code);
		
		System.out.println(JSONObject.toJSON(country));
	}
	
}
