package org.bird.breeze.web.member.bean;

import java.sql.Timestamp;

public class MemberBean {
	
	private String userName;
	private String password;
	private Timestamp lastLoginTime;
	private Timestamp passwordUpdatedTime;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Timestamp getPasswordUpdatedTime() {
		return passwordUpdatedTime;
	}
	public void setPasswordUpdatedTime(Timestamp passwordUpdatedTime) {
		this.passwordUpdatedTime = passwordUpdatedTime;
	}
	
	
}
