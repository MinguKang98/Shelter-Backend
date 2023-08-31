package com.example.shelter.news.crawler;

import com.example.shelter.news.News;
import com.example.shelter.news.repository.NewsRepository;
import com.example.shelter.news.selenium.Selenium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class YnaCrawler implements Crawler {

    private final NewsRepository newsRepository;
    private final Selenium selenium;
    private final String NEWS_URL = "https://www.yna.co.kr/search/index";
    private final String WRITER = "연합뉴스";
    private final List<String> NEWS_TYPES = List.of("지진", "쓰나미", "민방위");

    @Transactional
    public void crawling(LocalDate start, LocalDate end) {
        List<News> newsList = new ArrayList<>();
        WebDriver driver = selenium.getDriver();

        try {
            for (String newsType : NEWS_TYPES) {
                newsList.addAll(getNewsList(driver, newsType, start, end));
            }
            newsList.sort(Comparator.comparing(News::getPublishTime));
            if (!newsList.isEmpty()) {
                newsRepository.saveAll(newsList);
            }
        } catch (Exception e) {
            log.error("crawling error : {}", e.getMessage());
        } finally {
            driver.quit();
        }
    }

    public List<News> getNewsList(WebDriver driver, String newsType, LocalDate start, LocalDate end) throws InterruptedException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strStart = start.format(dateTimeFormatter);
        String strEnd = end.format(dateTimeFormatter);

        String url = UriComponentsBuilder.fromHttpUrl(NEWS_URL)
                .queryParam("query", newsType)
                .queryParam("ctype", "A")
                .queryParam("scope", "title")
                .queryParam("from", strStart)
                .queryParam("to", strEnd)
                .queryParam("period", "diy")
                .queryParam("page_no", 1)
                .build()
                .toUriString();

        driver.get(url);
        Document document = Jsoup.parse(driver.getPageSource());
        String strTotal = document.getElementById("article_list").select("div.search_news_list_title > span.total").text();
        int totalCount = Integer.parseInt(strTotal.substring(4, strTotal.length() - 3));
        int totalPage = totalCount / 10 + 1;

        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            String tempUrl = UriComponentsBuilder.fromHttpUrl(NEWS_URL)
                    .queryParam("query", newsType)
                    .queryParam("ctype", "A")
                    .queryParam("scope", "title")
                    .queryParam("from", strStart)
                    .queryParam("to", strEnd)
                    .queryParam("period", "diy")
                    .queryParam("page_no", i)
                    .build()
                    .toUriString();
            driver.navigate().to(tempUrl);
            Document tempDocument = Jsoup.parse(driver.getPageSource());
            Elements rawNewsList = tempDocument.select("div.cts_atclst > ul > li > a");

            for (Element rawNews : rawNewsList) {
                String href = rawNews.attr("href");
                Document click = click(driver, "https:" + href);
                String title = click.select("header.title-article01 > h1.tit").text();
                String strTime = click.select("header.title-article01 > p.update-time").text().substring(4);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime time = LocalDateTime.parse(strTime, formatter);
                Elements article = click.select("div.scroller01 > article > p");
                StringBuilder sb = new StringBuilder();
                for (Element element : article) {
                    sb.append("<p>");
                    sb.append(element.text());
                    sb.append("</p>");
                }
                String content = sb.toString();

                News news = News.builder()
                        .publishTime(time)
                        .writer(WRITER)
                        .title(title)
                        .content(content)
                        .build();
                newsList.add(news);
            }
        }
        return newsList;
    }

}
