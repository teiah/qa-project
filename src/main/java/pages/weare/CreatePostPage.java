package pages.weare;

import org.openqa.selenium.WebDriver;
import pages.BasePage;


public class CreatePostPage extends BasePage {
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
