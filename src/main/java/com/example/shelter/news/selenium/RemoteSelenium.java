package com.example.shelter.news.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Component
public class RemoteSelenium implements Selenium {

    @Value("${url.remote_driver}")
    private String REMOTE_DRIVER_URL;

    @Override
    public WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("window-size=1920x1080");
        options.addArguments("disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = null;
        try {
            URL url = new URL(REMOTE_DRIVER_URL + "/wd/hub");
            driver = new RemoteWebDriver(url, options);
        } catch (IOException e) {
            log.error("remote selenium url error : {}", e.getMessage());
        }
        return driver;
    }
}
