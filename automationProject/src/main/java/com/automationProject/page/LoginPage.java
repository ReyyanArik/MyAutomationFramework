package com.automationProject.page;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.automationProject.page.base.BasePage;

public class LoginPage extends BasePage{

	public LoginPage() {
		super();
	}
	
	private By user_name = By.id("");
	private By password = By.id("");
	private By sign_in_button = By.id("");

	private synchronized void setUserName(String username) {
		sendTextToElement(user_name, username, 20);
	}
	
	private synchronized void setPassword(String pass) {
		sendTextToElement(password, pass, 20);
	}
	
	private synchronized void clickSignInButton() {
		clickWebElement(sign_in_button, 20);
	}
	
	public MainPage login(String username, String pass) {
		setUserName(username);
		setPassword(pass);
		clickSignInButton();
		AssertTrue(getPageTitle().equals(""), "Login Unsuccessful!");
		return new MainPage();
	}
	

	
	
}
