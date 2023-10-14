package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class LatestPostsPage extends BaseWearePage {

    public LatestPostsPage(WebDriver driver) {
        super(driver, "weare.latestPostsPage");
    }


    public void assertPostIsCreated(String text) {
        actions.waitForElementPresent("weare.latestPostsPage.postMessage", text);
    }

    public void clickLikeButton(Integer postId) {
        actions.waitForElementClickable("weare.latestPostsPage.likeButton", postId.toString());
        actions.clickElement("weare.latestPostsPage.likeButton", postId.toString());
    }

    public void assertPostIsLiked(Integer postId) {
        actions.waitForElementVisible("weare.latestPostsPage.likesCount", postId.toString(), 1);

        if (!actions.isElementVisible("weare.latestPostsPage.likesCount", postId.toString(), 1)) {
            throw new AssertionError("Incorrect amount of likes.");
        }
    }

    public void explorePost(Integer postId) {
        actions.waitForElementVisible("weare.latestPostPage.exploreThisPostButton", postId.toString());
        actions.clickElement("weare.latestPostPage.exploreThisPostButton", postId.toString());
    }
}
