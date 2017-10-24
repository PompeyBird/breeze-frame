package org.bird.breeze.web.test.dao;

import java.util.List;

import org.bird.breeze.web.test.bean.Country;

public interface ICountryDao {
	
	public Country getCountry(String countryCode);
	
	public List<Country> queryCountry(Country country);
	
	public void addCountry(Country country);
	
	public void updateCountry(Country country);
	
	public void deleteCountry(String countryCode);
	
}
