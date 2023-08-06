package com.example.shelter.news.crawler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class YnaCrawlerTest {

    @Autowired
    YnaCrawler ynaCrawler;

//    @Test
//    @Rollback(value = false)
    public void crawling_테스트() {
        //given

        ///when
        LocalDate start = LocalDate.of(2023, 8, 6);
        LocalDate end = LocalDate.of(2023, 8, 6);
        ynaCrawler.crawling(start, end);

        //then
    }


}