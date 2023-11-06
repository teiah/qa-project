package pages.weare;

import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver, "weare.baseUrl");
    }

    public void assertUserHasLoggedIn() {
        if (!isLoggedIn()) {
            throw new AssertionError("Error: User has not successfully logged in. Logout button is not present.");
        }
    }

    public boolean isLoggedIn() {
        return actions.isElementPresent("weare.homePage.logoutButton");
    }

    public void logout() {
        actions.waitForElementClickable("weare.homePage.logoutButton");
        actions.clickElement("weare.homePage.logoutButton");
    }

    public void clickAddNewPost() {
        actions.waitForElementClickable("weare.homePage.addNewPostButton");
        actions.clickElement("weare.homePage.addNewPostButton");
    }

    public void clickLatestPosts() {
        actions.waitForElementClickable("weare.homePage.LatestPostsLink");
        actions.clickElement("weare.homePage.LatestPostsLink");
    }
}
