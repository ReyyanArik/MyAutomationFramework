package com.automationProject.util;

import java.util.ArrayList;
import java.util.List;

public enum MailPropertyType {
	SERVER_IP("server_ip"),
	MAIL_FROM("mail_from"),
	MAIL_TO("mail_to"),
	MAIL_CC("mail_cc"),
	MAIL_BCC("mail_bcc"),;

	private String value;
	
	private MailPropertyType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public static MailPropertyType findByValue(String value) {
		for(MailPropertyType item : MailPropertyType.values()) {
			if(item.value.equals(value)) {
				return item;
			}
		}
		return null;
	}
	
	public static List<String> getAllValues() {
		List<String> values = new ArrayList<>();
		for (MailPropertyType property : MailPropertyType.values()) {
			values.add(property.getValue());
		}
		return values;
	}
}
