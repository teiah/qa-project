package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telerikacademy.testframework.Utils.getUIMappingByKey;

public class RegisterPage extends BaseWearePage {
    public RegisterPage(WebDriver driver) {
        super(driver, "weare.registerPage");
    }

    public void registerUser(String username, String email, String password) {
        navigateToPage();
        assertPageNavigated();

        actions.waitForElementClickable("weare.registerPage.username");
        actions.clickElement("weare.registerPage.username");
        actions.typeValueInField(username, "weare.registerPage.username");

        actions.clickElement("weare.registerPage.email");
        actions.typeValueInField(email, "weare.registerPage.email");

        actions.clickElement("weare.registerPage.password");
        actions.typeValueInField(password, "weare.registerPage.password");

        actions.clickElement("weare.registerPage.confirm");
        actions.typeValueInField(password, "weare.registerPage.confirm");

        actions.clickElement("weare.registerPage.registerButton");
        actions.waitForElementClickable("weare.registerPage.pleaseUpdateYourProfileButton");
    }

    public String extractUserId() {

        actions.waitForElementClickable("weare.registerPage.pleaseUpdateYourProfileButton");
        String attributeName = "href";
        String hrefValue = actions.getElementAttribute(
                getUIMappingByKey("weare.registerPage.pleaseUpdateYourProfileButton"), attributeName);

        Pattern pattern = Pattern.compile("/auth/users/(\\d+)/");
        Matcher matcher = pattern.matcher(hrefValue);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }

    }
}
