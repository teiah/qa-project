package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class LatestPostsPage extends BaseWearePage{

    public LatestPostsPage(WebDriver driver) {
        super(driver, "weare.latestPostsPage");
    }


    public void assertPostIsCreated(String text) {

    actions.waitForElementPresent("weare.latestPostPage.postMessage", text);

    }
}
