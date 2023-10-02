package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

import static com.telerikacademy.testframework.Utils.getConfigPropertyByKey;

public class LoginPage extends BaseWearePage {
    public LoginPage(WebDriver driver) {
        super(driver, "weare.loginPage");
    }

    public void loginUser() {
        String username = getConfigPropertyByKey("weare.username");
        String password = getConfigPropertyByKey("weare.password");
    }
}
