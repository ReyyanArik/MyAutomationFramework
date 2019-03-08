package com.automationProject.util;

import java.util.ArrayList;
import java.util.List;

public enum AppPropertyType {
	
	BASE_URL("baseurl"),
	USERNAME("username"),
	PASSWORD("password"),
	REMOTE("remote"),
	GRID_URL("gridURL"),
	BROWSER("browser");

	
	private String value;
	
	private AppPropertyType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public static AppPropertyType findByValue(String value) {
		for(AppPropertyType item : AppPropertyType.values()) {
			if(item.value.equals(value)) {
				return item;
			}
		}
		return null;
	}
	
	public static List<String> getAllValues() {
		List<String> values = new ArrayList<>();
		for (AppPropertyType property : AppPropertyType.values()) {
			values.add(property.getValue());
		}
		return values;
	}

}