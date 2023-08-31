package com.example.shelter.news.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class LocalSelenium implements Selenium {

    @Value("${files.chrome_driver}")
    private String DRIVER_PATH;

    @Override
    public WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        return driver;
    }

}
