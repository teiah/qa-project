package pages.weare;

import org.openqa.selenium.*;

public class DeletePostPage extends BaseWearePage {

    public DeletePostPage(WebDriver driver, Integer postId) {
        super(driver, "weare.deletePostPage", postId);
    }

    public void deletePost() {
        actions.scrollToElement("weare.deletePostPage.confirmationSelector");
        actions.waitForElementClickable("weare.deletePostPage.confirmationSelector");
        actions.clickElementWithJavascript("weare.deletePostPage.confirmationSelector");
        actions.waitForElementClickable("weare.deletePostPage.deleteOption");
        actions.clickElement("weare.deletePostPage.deleteOption");
        actions.waitForElementClickable("weare.deletePostPage.submitButton");
        actions.clickElementWithJavascript("weare.deletePostPage.submitButton");
    }
}
