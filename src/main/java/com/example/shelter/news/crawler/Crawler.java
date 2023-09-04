package com.example.shelter.news.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

public interface Crawler {

    void crawling(LocalDate start, LocalDate end);

    default Document click(WebDriver driver, String url) throws InterruptedException {
        driver.navigate().to(url);
        Thread.sleep(1000);
        Document parse = Jsoup.parse(driver.getPageSource());
        driver.navigate().back();
        return parse;
    }

}
