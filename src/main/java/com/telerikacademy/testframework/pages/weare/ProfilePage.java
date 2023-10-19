package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class ProfilePage extends BaseWearePage {

    public ProfilePage(WebDriver driver, Integer userId) {
        super(driver, "weare.profilePage", userId);
    }

    public void sendRequest() {
        actions.waitForElementVisible("weare.profilePage.connectButton");
        actions.clickElement("weare.profilePage.connectButton");
        actions.assertElementPresent("weare.profilePage.sentRequestMessage");
    }

    public void disconnect() {
        actions.waitForElementVisible("weare.profilePage.disconnectButton");
        actions.clickElement("weare.profilePage.disconnectButton");
        actions.assertElementPresent("weare.profilePage.connectButton");
    }

    public void seeRequests() {
        actions.waitForElementClickable("weare.profilePage.newFriendRequestsButton");
        actions.clickElement("weare.profilePage.newFriendRequestsButton");
    }

    public void logout() {
        actions.waitForElementClickable("weare.profilePage.logoutButton");
        actions.clickElement("weare.profilePage.logoutButton");
    }
}
