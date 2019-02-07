package com.automationProject.page.base;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.xalan.xsltc.compiler.sym;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.automationProject.base.BaseTest;

public class BasePage extends BaseTest {

	protected WebDriver driver = null;
	private WebDriverWait jsWait = null;
	private JavascriptExecutor jsExec = null;

	protected static final Logger log = Logger.getLogger(BasePage.class.getName());	
	public BasePage() {
		driver = getDriver();
		jsWait = new WebDriverWait(driver, 90);
		jsExec = (JavascriptExecutor) driver;
	}

	public void waitForElementToBeGone(By locator) {
		WebElement element = driver.findElement(locator);
		if (isElementDisplayed(element)) {
			waitElementToDisappear(element, 120);
		}
	}

	public boolean isElementDisplayed(WebElement element) {
		try {
			jsWait.until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		} catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException
				| org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public boolean isLocatorDisplayed(By locator) {
		WebElement element = getWebElementByLocator(locator);
		try {
			jsWait.until(ExpectedConditions.visibilityOf(element));
			highlightElement(locator);
			return element.isDisplayed();
		} catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException
				| org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public synchronized WebElement waitUntilElementFound(By locator, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		return wait.until(f -> f.findElement(locator));
	}

	public WebElement highlightElement(By locator) {
		WebElement elem = driver.findElement(locator);
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", elem);
		}
		return elem;
	}

	public synchronized WebElement waitUntilClickableByLocator(By locator, int timeOut) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public synchronized WebElement getWebElementByLocator(By locator) {
		return driver.findElement(locator);
	}
	
	protected synchronized String getPageTitle() {
		return driver.getTitle();
	}

	public void sleep(int second) {

		try {

			log.info("SLEEP...");
			long duration = new Long(second).longValue();
			Sleeper.SYSTEM_SLEEPER.sleep(
					new org.openqa.selenium.support.ui.Duration(duration, java.util.concurrent.TimeUnit.SECONDS));
			log.info("Time to wake up!");

		} catch (Exception e) {
			log.info("Sleep Error :" + e);
		}

	}

	public void clickWebElement(By locator, int timeOut) {

		waitUntilVisibleByLocator(locator, timeOut);
		WebElement element = waitUntilClickableByLocator(locator, timeOut);
		if (element == null) {
			waitUntilElementFound(locator, timeOut);
		}
		findAndScrollWebElementByLocator(locator, 5);
		element = waitUntilClickableByLocator(locator, timeOut);
		if (!checkElementIsEnabledByLocator(locator)) {
			sleep(5);
		}
		jsExec.executeScript("arguments[0].style.backgroundColor = 'red'", element);
		getWebElementByLocator(locator).click();

	}

	protected synchronized WebElement waitUntilVisibleByLocator(By locator, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected synchronized WebElement waitUntilPresenceOfElementByLocator(By locator, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));

	}

	protected synchronized void sendTextToElement(By locator, String text, int timeOut) {

		waitUntilPresenceOfElementByLocator(locator, timeOut);
		waitUntilVisibleByLocator(locator, timeOut);
		waitUntilElementFound(locator, timeOut).sendKeys(text);
		waitUntilJQueryReadyAndJSReady();

	}

	protected synchronized void sendKeyToElement(By locator, Keys key, int timeOut) {

		waitUntilPresenceOfElementByLocator(locator, timeOut);
		waitUntilVisibleByLocator(locator, timeOut);
		waitUntilElementFound(locator, timeOut).sendKeys(key);
		waitUntilJQueryReadyAndJSReady();

	}

	public synchronized void clearWebElement(By locator, int timeOut) {

		waitUntilPresenceOfElementByLocator(locator, timeOut);
		waitUntilVisibleByLocator(locator, timeOut);
		waitUntilElementFound(locator, timeOut).clear();

	}

	protected synchronized void waitForJQueryLoad() {
		ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) driver)
				.executeScript("return jQuery.active") == 0);
		boolean jqueryReady = (Boolean) jsExec.executeScript("return window.jQuery != 'undefined' || jQuery.active==0");
		if (!jqueryReady) {
			jsWait.until(jQueryLoad);
		}
	}

	protected synchronized void waitForAngularJSLoad() {

		String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

		ExpectedCondition<Boolean> angularLoad = driver -> Boolean
				.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());

		boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

		if (!angularReady) {
			jsWait.until(angularLoad);
		}
	}

	protected synchronized void waitUntilJSReady() {

		ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver)
				.executeScript("return document.readyState").toString().equals("complete");

		boolean jsReady = jsExec.executeScript("return document.readyState").toString().equals("complete");

		if (!jsReady) {
			jsWait.until(jsLoad);
		}

	}

	protected synchronized void waitUntilJQueryReadyAndJSReady() {
		waitForJQueryLoad();
		waitUntilJSReady();
	}

	protected synchronized void waitUntilAngularReady() throws InterruptedException {
		Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
		if (!angularUnDefined) {
			Boolean angularInjectorUnDefined = (Boolean) jsExec
					.executeScript("return angular.element(document).injector() === undefined");
			if (!angularInjectorUnDefined) {
				sleep(1);
				waitForAngularJSLoad();
				waitUntilJSReady();
				sleep(1);
			} else {
				log.info("Angular injector is not defined on this site!");
			}
		} else {
			log.info("Angular is not defined on this site!");
		}
	}

	protected synchronized WebElement waitUntilClickableByListOfElement(WebElement webElement, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.elementToBeClickable(webElement));

	}

	protected synchronized boolean waitUntilUrlContains(String expectedValue, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.urlContains(expectedValue)).booleanValue();
	}

	protected synchronized void waitUntilFrameToBeAvailableAndSwitchToIt(String frameName, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class)
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
	}

	protected synchronized List<WebElement> getListOfElementsByLocator(By locator) {
		return driver.findElements(locator);
	}

	protected synchronized int getSizeOfWebElementListByLocator(By locator) {
		return driver.findElements(locator).size();
	}

	protected synchronized boolean checkElementIsExistsByLocator(By locator) {
		return driver.findElement(locator) != null;
	}

	protected synchronized boolean checkElementIsDisplayedByLocator(By locator) {
		return driver.findElement(locator).isDisplayed();
	}

	protected synchronized boolean checkElementIsEnabledByLocator(By locator) {
		return driver.findElement(locator).isEnabled();
	}

	protected synchronized boolean checkElementIsSelectedByLocator(By locator) {
		return driver.findElement(locator).isSelected();
	}

	protected synchronized void findAndScrollWebElementByLocator(By locator, int scrollAmount) {
		String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
				+ "var elementTop = arguments[0].getBoundingClientRect().top;"
				+ "window.scrollBy(0, elementTop-(viewPortHeight/" + scrollAmount + "));";
		jsExec.executeScript(scrollElementIntoMiddle, getWebElementByLocator(locator));
	}

	protected void scrollTo(WebElement element, int margin) {
		scrollTo(element.getLocation().x, element.getLocation().y - margin);
	}

	protected void scrollTo(int x, int y) {
		jsExec.executeScript("scrollTo(" + x + "," + y + ");");
	}

	protected synchronized boolean verifyContains(String str1, String str2) {
		return str1.toUpperCase().contains(str2.toUpperCase());
	}

	public synchronized boolean isElementPresent(By by, int timeOut) {
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NotFoundException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.presenceOfElementLocated(by)) != null;
	}
	
	protected synchronized List<WebElement> visibilityOfAllWait(By by, int timeOut) {
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NotFoundException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
	}

	public synchronized void waitForElementToHide(By elementLocator, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NotFoundException.class)
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));
	}

	protected synchronized void clickWebElement(WebElement element, int timeOut) {
		scrollTo(element, 100);
		waitUntilClickableByListOfElement(element, timeOut).click();
		waitUntilJQueryReadyAndJSReady();
	}

	protected synchronized String getText(By byLocator, int timeOut) {
		waitUntilVisibleByLocator(byLocator, timeOut);
		return driver.findElement(byLocator).getText();
	}

	protected synchronized List<String> getAllTexts(By byLocator, int timeOut) {
		List<String> allTexts = new ArrayList<>();
		waitUntilVisibleByLocator(byLocator, timeOut);
		for (WebElement eachElement : driver.findElements(byLocator)) {
			allTexts.add(eachElement.getText());
		}
		return allTexts;
	}

	protected synchronized List<WebElement> visibilitiesWaitNested(WebElement element, By by, int timeOut) {

		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NotFoundException.class)
				.ignoring(NoSuchElementException.class);
		return wait.until(visibilityOfAllElementsLocatedByIn(by, element));
	}

	protected synchronized ExpectedCondition<List<WebElement>> visibilityOfAllElementsLocatedByIn(final By locator,
			final WebElement parent) {

		return new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver driver) {
				List<WebElement> elements;
				if (parent != null) {
					elements = parent.findElements(locator);
				} else {
					elements = driver.findElements(locator);
				}
				for (WebElement element : elements) {
					if (!element.isDisplayed()) {
						return emptyList();
					}
				}
				return !elements.isEmpty() ? elements : null;
			}

			@Override
			public String toString() {
				return "visibility of all elements located by " + locator;
			}
		};
	}

	protected synchronized boolean isElementPresentInside(WebElement element, By by) {

		return !element.findElements(by).isEmpty();
	}

	protected synchronized void waitForElementToGetAttribute(int seconds, By elementLocator, String attribute,
			String value) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.attributeContains(elementLocator, attribute, value));
	}

	protected synchronized void waitElementToDisappear(WebElement element, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.stalenessOf(element));
	}

	protected synchronized boolean isTextContain(String searchedText, int timeOut) {
		try {
			waitUntilVisibleByLocator(By.xpath("//*[contains(text(),'" + searchedText + "')]"), timeOut);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	
	protected <T> T executeScript(String script) {
		return (T) jsExec.executeScript(script);
	}

	protected synchronized void waitUntilFinishLoader(int presenceTimeOut, int hideTimeOut, By loader) {

		log.info("Loader is Opened...");
		waitForElementToHide(loader, hideTimeOut);
		log.info("Loader is Closed...");
		waitUntilJQueryReadyAndJSReady();

	}
	
	protected synchronized WebElement selectOptionBy(By locator, String value) {
		WebElement element = getWebElementByLocator(locator);
		new Select(element).selectByVisibleText(value);
		log.info(value+" is selected.");
		return element;
	}
	
	protected synchronized void AssertTrue(Boolean condition, String message) {
		Assert.assertTrue(condition, message);
	}
	


}
