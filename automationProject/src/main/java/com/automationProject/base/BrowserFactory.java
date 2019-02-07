package com.automationProject.base;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.TestException;


public class BrowserFactory extends BaseTest {
		
		public WebDriver prepareDriver() {
			WebDriver driver;

			if (getAppConfiguration().getRemote().equals("true")) {
				driver = getRemoteDriver();
			} else {
				driver = getLocalDriver();
			}
			return driver;
		}
		
		private WebDriver getRemoteDriver() {
			WebDriver webDriver;
			DesiredCapabilities cap = getRemoteCapabilities();
			try {
				System.out.println(getAppConfiguration().getGridURL());
				webDriver = new RemoteWebDriver(new URL(getAppConfiguration().getGridURL()), cap);
				
				((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
			} catch (MalformedURLException mue) {
				throw new TestException(mue);
			}
			return webDriver;
		}

		private DesiredCapabilities getRemoteCapabilities() {
			DesiredCapabilities capabilities;
			if ("firefox".equals(getAppConfiguration().getBrowser())) {
				capabilities = DesiredCapabilities.firefox();

				FirefoxProfile profile = new FirefoxProfile();
				profile.setAcceptUntrustedCertificates(true);
				profile.setAssumeUntrustedCertificateIssuer(false);
				profile.setPreference("browser.privatebrowsing.autostart", true);
				profile.setPreference("network.proxy.type", 0);

				capabilities.setCapability(FirefoxDriver.PROFILE, profile);
				return capabilities;
				
			} else if ("ie".equals(getAppConfiguration().getBrowser())) {
				return DesiredCapabilities.internetExplorer();
				
			} else {
				capabilities = DesiredCapabilities.chrome();
				
				ChromeOptions chromeOptions = new ChromeOptions();
				Map<String, Object> prefs = new HashMap<>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				chromeOptions.setExperimentalOption("prefs", prefs);
				chromeOptions.addArguments("-disable-cache");
				chromeOptions.addArguments("--incognito");
				chromeOptions.addArguments("--start-maximized");
				capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

				return capabilities;
			}
		}

		private WebDriver getLocalDriver() {
			switch (getAppConfiguration().getBrowser()) {
			case "chrome":
				return new ChromeDriver();
//			case "ie":
//				return new InternetExplorerDriver();
			default:
				return new ChromeDriver();
			}
		}



	}