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

        WebElement messageField = driver.findElement(By.id("message"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", messageField);

        actions.typeValueInField(text, "weare.createPostPage.messageField");
        actions.clickElement("weare.createPostPage.savePostButton");

    }
}
