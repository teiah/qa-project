package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class PostPage extends BaseWearePage {

    public PostPage(WebDriver driver, Integer postId) {
        super(driver, "weare.postPage", postId);
    }

    public void clickEditPost() {
        actions.waitForElementVisible("weare.postPage.editPostButton");
        actions.clickElement("weare.postPage.editPostButton");
    }

    public boolean messageIs(String message) {
        return actions.isElementPresent("weare.postPage.postMessage", message);
    }

    public void createComment(String text) {

    }
}
