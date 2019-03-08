package com.automationProject.test;

import org.testng.annotations.Test;

import com.automationProject.base.BaseTest;
import com.automationProject.page.LoginPage;

public class LoginPageTest extends BaseTest{

	@Test
	public void successLoginTest() {
		new LoginPage().login(USERNAME, PASS);
	}
}
