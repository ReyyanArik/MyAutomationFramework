package com.automationProject.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Utilities {
	private Utilities() {
	}
	protected static final Logger log = Logger.getLogger(Utilities.class);
	public static Properties getProperty(String path) {
		Properties properties = new Properties();
		FileReader filereader = null;
		try {
			filereader = new FileReader(path);
			properties.load(filereader);
		} catch (IOException e) {
			log.info("Property read fail!");
		}
		return properties;
	}
}