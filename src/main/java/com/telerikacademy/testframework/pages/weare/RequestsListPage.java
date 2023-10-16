package com.telerikacademy.testframework.pages.weare;

import org.openqa.selenium.WebDriver;

public class RequestsListPage extends BaseWearePage{
    public RequestsListPage(WebDriver driver, String pageUrlKey, Object... arguments) {
        super(driver, pageUrlKey, arguments);
    }

    public void approveRequest(String firstName) {
        actions.assertElementPresent("weare.requestsListPage.requestSenderInfo", firstName);
        actions.waitForElementPresent("weare.requestsListPage.approveRequestButton");
        actions.submitElement("weare.requestsListPage.approveRequestButton");
        actions.assertElementPresent("weare.requestsListPage.noRequests");
    }

    public void logout() {
        actions.waitForElementClickable("weare.requestsListPage.logout");
        actions.clickElement("weare.requestsListPage.logout");
    }
}
