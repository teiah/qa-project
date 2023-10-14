package com.telerikacademy.testframework.pages.weare;

import com.telerikacademy.testframework.*;
import com.telerikacademy.testframework.pages.BasePage;
import org.openqa.selenium.WebDriver;

public abstract class BaseWearePage extends BasePage {

    public BaseWearePage(WebDriver driver, String pageUrlKey, Object... arguments) {
        super(driver, pageUrlKey, arguments);
    }

}
