package org.bird.breeze.web.test.service;

import org.bird.breeze.web.test.bean.Country;
import com.github.pagehelper.PageInfo;

public interface ICountrySvc {
	
	public PageInfo<Country> queryContryList(Country country, int pageIndex, int pageSize);
	
	public Country getCountry(String countryCode);
	
}
