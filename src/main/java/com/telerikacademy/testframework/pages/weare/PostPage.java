package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.telerikacademy.testframework.utils.Utils.getUIMappingByKey;

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

    public void createComment(String comment) {

        actions.scrollToElement("weare.postPage.commentMessage");
        actions.waitForElementClickable("weare.postPage.commentMessage");
        actions.typeValueInField(comment, "weare.postPage.commentMessage");
        actions.clickElement("weare.postPage.postComment");

    }

    public void assertPostCommentsCountUpdates(String expectedCount) {
        String commentCount = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentsCount")))).getAttribute("innerHTML");
        System.out.println("comment count is:"+ commentCount);
        Assert.assertEquals(commentCount, expectedCount, "Wrong comment count");


    }
}

