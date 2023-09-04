package com.example.shelter.news.service;

import com.example.shelter.exception.notfound.NewsNotFoundException;
import com.example.shelter.news.News;
import com.example.shelter.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News findById(Long id) {
        return newsRepository.findByIdNotDeleted(id).orElseThrow(() -> new NewsNotFoundException(id));
    }

    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAllNotDeleted(pageable);
    }

    public Optional<News> findPrevByNews(News news) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<News> newsList = newsRepository.findPrevByIdNotDeleted(news.getId(), pageRequest);
        return newsList.size() == 0 ? Optional.empty() : Optional.of(newsList.get(0));
    }

    public Optional<News> findNextByNews(News news) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<News> newsList = newsRepository.findNextByIdNotDeleted(news.getId(), pageRequest);
        return newsList.size() == 0 ? Optional.empty() : Optional.of(newsList.get(0));
    }

}
