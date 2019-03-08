package com.automationProject.base;

import org.openqa.selenium.support.events.EventFiringWebDriver;

public class DriverManager {

	private ThreadLocal<EventFiringWebDriver> driverThread = new ThreadLocal<>();

	public DriverManager() {
		super();
	}

	public EventFiringWebDriver  getDriver() {
		return driverThread.get();
	}
	
	public void setDriver(EventFiringWebDriver driver) {
		driverThread.set(driver);
	}

}