package com.automationProject.base;

import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.automationProject.util.AppConfiguration;
import com.automationProject.util.AppPropertyType;
import com.automationProject.util.Utilities;
import com.automationProject.base.DriverManager;

public class BaseTest {

	protected WebDriver driver;

	protected static final Logger logTest = Logger.getLogger(BaseTest.class);
	private static DriverManager driverManager = null;
	private static AppConfiguration appConfiguration = null;

	public static String BASEURL = null;
	public static String USERNAME = null;
	public static String PASS = null;
	public static String remote = null;
	public static String gridURL = null;
	public static String browser = null;

	@BeforeSuite
	public void beforeSuite() {
		initializeconfig();
	}

	@BeforeMethod
	public void beforeMethod() throws MalformedURLException {
		BrowserFactory browser = new BrowserFactory();
		EventFiringWebDriver driver = new EventFiringWebDriver(browser.prepareDriver());
		driverManager = new DriverManager();
		driverManager.setDriver(driver);
		driverManager.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driverManager.getDriver().manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		driverManager.getDriver().manage().window().maximize();
		driverManager.getDriver().navigate().to(BASEURL);
	}

	@AfterMethod
	public void afterMethod() {
		getDriver().quit();
	}

	public synchronized void initializeconfig() {

		Properties config = Utilities.getProperty("properties/application.properties");

		appConfiguration = new AppConfiguration();

		BASEURL = String.valueOf(config.getProperty(AppPropertyType.BASE_URL.getValue()));
		appConfiguration.setBaseurl(BASEURL);

		USERNAME = String.valueOf(config.getProperty(AppPropertyType.USERNAME.getValue()));
		appConfiguration.setUsername(USERNAME);
		
		PASS = String.valueOf(config.getProperty(AppPropertyType.PASSWORD.getValue()));
		appConfiguration.setPassword(PASS);

		remote = String.valueOf(config.getProperty(AppPropertyType.REMOTE.getValue()));
		appConfiguration.setRemote(remote);

		gridURL = String.valueOf(config.getProperty(AppPropertyType.GRID_URL.getValue()));
		appConfiguration.setGridURL(gridURL);

		browser = String.valueOf(config.getProperty(AppPropertyType.BROWSER.getValue()));
		appConfiguration.setBrowser(browser);

	}

	public AppConfiguration getAppConfiguration() {
		return appConfiguration;
	}

	public synchronized WebDriver getDriver() {
		return driverManager.getDriver();
	}

}
