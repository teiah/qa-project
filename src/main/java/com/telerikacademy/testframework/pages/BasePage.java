package com.telerikacademy.testframework.pages;

import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.utils.Utils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import static java.lang.String.format;

public abstract class BasePage {

    protected String url;
    protected WebDriver driver;
    public UserActions actions;

    public BasePage(WebDriver driver, String urlKey, Object... arguments) {
        String pageUrl = format(Utils.getConfigPropertyByKey(urlKey), arguments);
        this.driver = driver;
        this.url = pageUrl;
        actions = new UserActions();
    }

    public String getUrl() {
        return url;
    }

    public void navigateToPage() {
        this.driver.get(url);
    }

    public void logout() {

        // To Do

    }

    public void assertPageNavigated() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(url), "Landed URL is not as expected. Actual URL: " + currentUrl + ". Expected URL: " + url);
    }

}
