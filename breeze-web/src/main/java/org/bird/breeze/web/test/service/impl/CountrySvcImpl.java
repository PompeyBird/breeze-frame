package org.bird.breeze.web.test.service.impl;

import java.util.List;

import org.bird.breeze.web.test.bean.Country;
import org.bird.breeze.web.test.dao.ICountryDao;
import org.bird.breeze.web.test.service.ICountrySvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class CountrySvcImpl implements ICountrySvc {

	@Autowired
	private ICountryDao iCountryDao;
	
	@Override
	public PageInfo<Country> queryContryList(Country country, int pageIndex, int pageSize) {
		
		PageHelper.startPage(pageIndex, pageSize);
		List<Country> list = iCountryDao.queryCountry(country);
		PageInfo<Country> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public Country getCountry(String countryCode) {
		
		return iCountryDao.getCountry(countryCode);
	}
	
	
	
	
	
}
