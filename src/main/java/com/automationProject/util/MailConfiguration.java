package com.automationProject.util;

public class MailConfiguration {
	private String server_ip;
	private String from;
	private String to;

	public String getServerIP() {
		return server_ip;
	}

	public void setServerIP(String server_ip) {
		this.server_ip = server_ip;
	}

	public String getFROM() {
		return from;
	}

	public void setFROM(String from) {
		this.from = from;
	}

	public String getTO() {
		return to;
	}

	public void setTO(String to) {
		this.to = to;
	}
}
