package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class HomePage extends BaseWearePage{
    public HomePage(WebDriver driver) {
        super(driver, "weare.homePage");
    }
    public void assertUserHasLoggedIn() {
        if (!actions.isElementPresent("weare.homePage.logoutButton")) {
            throw new AssertionError("Error: User has not successfully logged in. Logout button is not present.");
        }
    }
}
