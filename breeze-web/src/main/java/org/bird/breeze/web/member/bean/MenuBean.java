package org.bird.breeze.web.member.bean;

import java.util.List;

public class MenuBean {

	private String title;
	private String code;
	private String src;
	private String parentCode;
	private Boolean hidden;
	
	private List<MenuBean> subMenus;
	private List<String> navigation;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public List<MenuBean> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<MenuBean> subMenus) {
		this.subMenus = subMenus;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public List<String> getNavigation() {
		return navigation;
	}

	public void setNavigation(List<String> navigation) {
		this.navigation = navigation;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
	
}
