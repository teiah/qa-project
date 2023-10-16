package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

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

    public void showComment() {

        actions.scrollToElement("weare.postPage.commentShow");
        actions.waitForElementClickable("weare.postPage.commentShow");
        actions.clickElementWithJavascript("weare.postPage.commentShow");
        actions.moveToElementAndClickOnIt("weare.postPage.commentShow");


    }

    public void editCommentNavigate() {

        //actions.scrollElementWithJavascript(("weare.postPage.commentEdit"));
        actions.waitForElementVisible("weare.postPage.commentEdit");
        //actions.clickElement("weare.postPage.commentEdit");
        actions.moveToElementAndClickOnIt("weare.postPage.commentEdit");

    }

    public void editComment(String editedComment) {


        //actions.scrollToElement("weare.postPage.commentMessageEdit");
        actions.waitForElementClickable("weare.postPage.commentMessageEdit");
        actions.typeValueInField(editedComment, "weare.postPage.commentMessageEdit");
        actions.clickElement("weare.postPage.commentEditPost");
    }

    public void deleteComment() {

        actions.waitForElementClickable("weare.postPage.commentDelete");
        actions.clickElement("weare.postPage.commentDelete");
        actions.waitForElementClickable("weare.postPage.commentDeleteConfirmBox");
        actions.clickElement("weare.postPage.commentDeleteConfirmBox");
        actions.waitForElementClickable("weare.postPage.commentDeleteConfirmOption");
        actions.clickElement("weare.postPage.commentDeleteConfirmOption");
        actions.clickElement("weare.postPage.commentDeleteSubmitButton");
    }

    public void likeComment() {

        actions.waitForElementClickable("weare.postPage.commentLikeButton");
        actions.clickElement("weare.postPage.commentLikeButton");

    }

    public void assertPostCommentsCountUpdates(String expectedCount) {
        actions.waitForElementVisible("weare.postPage.commentCount");
        String commentCount = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentCount")))).getAttribute("innerHTML");
        System.out.println("comment count is:" + commentCount);
        Assert.assertEquals(commentCount, expectedCount, "Wrong comment count");
    }

    public void assertPostCommentsAuthorExists(String expectedAuthor) {
        String commentAuthor = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentAuthor")))).getAttribute("innerHTML");
        System.out.println("comment author is:" + commentAuthor);
        Assert.assertEquals(commentAuthor, expectedAuthor, "Wrong comment author");
    }

    public void assertPostCommentTextExists(String expectedText) {
        String commentText = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentText")))).getAttribute("innerHTML");
        System.out.println("comment text is:" + commentText);
        Assert.assertEquals(commentText, expectedText, "Wrong comment text");
    }

    public void assertPostCommentEditedTextExists(String expectedEditedText) {
        String commentText = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentText")))).getAttribute("innerHTML");
        System.out.println("comment text is:" + commentText);
        Assert.assertEquals(commentText, expectedEditedText, "Wrong comment text");
    }

    public void assertPostCommentDeleted() {
        actions.waitForElementVisible("weare.postPage.commentDeleteConfirmText");
        String confirmText = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentDeleteConfirmText")))).getAttribute("innerHTML");
        System.out.println("Confirmation text is:" + confirmText);
        Assert.assertEquals(confirmText, "Comment deleted successfully", "Wrong comment text");
//        actions.waitForElementVisible("weare.postPage.commentDeleteConfirmText");
//        Assert.assertTrue(actions.isElementPresent("weare.postPage.commentDeleteConfirmText"),
//                "Deletion confirmation is not present");
    }

    public void assertPostCommentLiked() {
        String confirmText = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentLikeCount")))).getAttribute("innerHTML");
        System.out.println("Confirmation text is:" + confirmText);
        Assert.assertEquals(confirmText, "Likes: 1", "Wrong comment text");
    }

    public void assertPostCommentDislikeButtonIsPresent() {
        Assert.assertTrue(actions.isElementPresent("weare.postPage.commentDislikeButton"),
                "Dislike button is not present");

    }

    public void assertDeleteCommentConfirmationTextExists() {
        String commentText = (driver.findElement(By.xpath(getUIMappingByKey("weare.postPage.commentDeleteConfirmText")))).getAttribute("innerHTML");
        System.out.println("comment text is:" + commentText);
        Assert.assertEquals(commentText, "Comment deleted successfully", "Wrong comment text");
    }
}

