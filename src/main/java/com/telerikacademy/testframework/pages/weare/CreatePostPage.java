package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreatePostPage extends BaseWearePage {
    public CreatePostPage(WebDriver driver) {
        super(driver, "weare.createPostPage");
    }

    public void createPost(String text) {
        actions.scrollToElement("weare.createPostPage.messageField");
        actions.waitForElementClickable("weare.createPostPage.messageField");
        actions.typeValueInField(text, "weare.createPostPage.messageField");
        actions.clickElement("weare.createPostPage.savePostButton");

    }
}
