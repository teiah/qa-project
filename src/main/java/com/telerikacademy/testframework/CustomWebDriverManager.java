package com.telerikacademy.testframework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CustomWebDriverManager {

    public enum CustomWebDriverManagerEnum {
        INSTANCE;
        private WebDriver driver = setupBrowser();

        public void quitDriver() {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }

        public WebDriver getDriver() {
            if (driver == null) {
                setupBrowser();
            }
            return driver;
        }

        private WebDriver setupBrowser() {
            String osName = System.getProperty("os.name").toLowerCase();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            if (osName.contains("mac")) {
                System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver-mac-arm64/chromedriver");
                WebDriver driver = new ChromeDriver(options);
                driver.manage().window().maximize();
                this.driver = driver;
                return driver;
            } else {
                WebDriver driver = new ChromeDriver(options);
                driver.manage().window().maximize();
                this.driver = driver;
                return driver;
            }
        }
    }
}
