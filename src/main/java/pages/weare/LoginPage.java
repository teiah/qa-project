package pages.weare;

import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        super(driver, "weare.loginPage");
    }

    public void loginUser(String username, String password) {
        navigateToPage();
        assertPageNavigated();
        // Fill in username
        actions.waitForElementVisible("weare.loginPage.username");
        actions.typeValueInField(username, "weare.loginPage.username");

        // Fill in password
        actions.waitForElementClickable("weare.loginPage.password");
        actions.typeValueInField(password, "weare.loginPage.password");

        // Click login button
        actions.waitForElementClickable("weare.loginPage.loginSubmitButton");
        actions.clickElement("weare.loginPage.loginSubmitButton");
    }
}
