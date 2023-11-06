package pages.weare;

import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class EditPostPage extends BasePage {

    public EditPostPage(WebDriver driver, Integer postId) {
        super(driver, "weare.editPostPage", postId);
    }

    public void editPostMessage(String message) {
        actions.scrollToElement("weare.createPostPage.messageField");
        actions.waitForElementClickable("weare.createPostPage.messageField");
        actions.typeValueInField(message, "weare.createPostPage.messageField");

    }

    public void editPostVisibility() {
        actions.waitForElementClickable("weare.createPostPage.visibilitySelector");
        actions.clickElement("weare.createPostPage.visibilitySelector");
        actions.clickElement("weare.createPostPage.publicVisibilityOption");
    }

    public void savePostChanges() {
        actions.clickElement("weare.createPostPage.savePostButton");
    }
}
