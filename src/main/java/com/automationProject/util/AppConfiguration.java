package com.automationProject.util;

public class AppConfiguration {
	private String baseurl;
	private String username;
	private String password;
	
	private String remote;
	private String gridURL;
	private String browser;
	
	public String getBaseurl() {
		return baseurl;
	}
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemote() {
		return remote;
	}
	public void setRemote(String remote) {
		this.remote = remote;
	}
	public String getGridURL() {
		return gridURL;
	}
	public void setGridURL(String gridURL) {
		this.gridURL = gridURL;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	@Override
	public String toString() {
		return "AppConfiguration [baseurl=" + baseurl + ", username=" + username + ", password=" + password
				+ ", remote=" + remote + ", gridURL=" + gridURL + ", browser=" + browser + "]";
	}
	
	
	
	
}
