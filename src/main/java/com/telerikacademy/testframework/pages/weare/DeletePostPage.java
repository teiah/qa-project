package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class DeletePostPage extends BaseWearePage {

    public DeletePostPage(WebDriver driver, Integer postId) {
        super(driver, "weare.deletePostPage", postId);
    }

    public void deletePost() {

        actions.waitForElementClickable("weare.deletePostPage.confirmationSelector");
        actions.clickElement("weare.deletePostPage.confirmationSelector");
        actions.clickElement("weare.deletePostPage.deleteOption");
        actions.waitForElementClickable("weare.deletePostPage.submitButton");
        actions.clickElement("weare.deletePostPage.submitButton");
    }
}
