package com.example.shelter.news.controller;

import com.example.shelter.news.News;
import com.example.shelter.news.crawler.YnaCrawler;
import com.example.shelter.news.dto.NewsDto;
import com.example.shelter.news.dto.NewsPageDto;
import com.example.shelter.news.dto.NewsTitleDto;
import com.example.shelter.news.service.NewsService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final YnaCrawler ynaCrawler;
    private final int PAGE_SIZE = 8;

    @GetMapping("/api/news/{id}")
    public ResponseEntity<NewsDto> getNews(@PathVariable("id") Long id) {
        News news = newsService.findById(id);
        return ResponseEntity.ok(NewsDto.of(news));
    }

    @GetMapping("/api/news")
    public ResponseEntity<NewsPageDto> getNewsPage(@RequestParam("page") @Positive int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "publishTime"));
        Page<News> news = newsService.findAll(pageRequest);
        return ResponseEntity.ok(NewsPageDto.of(news));
    }

    @GetMapping("/api/news/{id}/prev")
    public ResponseEntity<NewsTitleDto> getPrevNews(@PathVariable("id") Long id) {
        News news = newsService.findById(id);
        Optional<News> prevNews = newsService.findPrevByNews(news);
        return ResponseEntity.ok(prevNews.map(NewsTitleDto::of).orElseGet(NewsTitleDto::empty));
    }

    @GetMapping("/api/news/{id}/next")
    public ResponseEntity<NewsTitleDto> getNextNews(@PathVariable("id") Long id) {
        News news = newsService.findById(id);
        Optional<News> nextNews = newsService.findNextByNews(news);
        return ResponseEntity.ok(nextNews.map(NewsTitleDto::of).orElseGet(NewsTitleDto::empty));
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void crawling() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ynaCrawler.crawling(yesterday, yesterday);
    }

}
