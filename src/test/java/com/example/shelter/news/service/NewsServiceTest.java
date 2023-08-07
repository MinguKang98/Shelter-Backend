package com.example.shelter.news.service;

import com.example.shelter.exception.notfound.NewsNotFoundException;
import com.example.shelter.news.News;
import com.example.shelter.news.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    NewsRepository newsRepository;

    @InjectMocks
    NewsService newsService;

    @Test
    public void findById_존재하는_뉴스_테스트() {
        //given
        Long id = 1L;
        News news = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();
        when(newsRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(news));

        ///when
        News findNews = newsService.findById(id);

        //then
        assertThat(findNews.getId()).isEqualTo(id);
        verify(newsRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_뉴스_테스트() {
        //given
        Long id = 1L;
        when(newsRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> newsService.findById(id)).isInstanceOf(NewsNotFoundException.class);
        verify(newsRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findAll_테스트() {
        //given
        int totalSize = 30;
        List<News> content = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            News tempNews = News.builder()
                    .id((long) i)
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목")
                    .content("내용")
                    .build();
            content.add(tempNews);
        }
        PageRequest pageRequest = PageRequest.of(0, 8);
        PageImpl<News> news = new PageImpl<>(content, pageRequest, totalSize);
        when(newsRepository.findAllNotDeleted(pageRequest)).thenReturn(news);

        ///when
        Page<News> findNews = newsService.findAll(pageRequest);

        //then
        assertThat(findNews.getTotalElements()).isEqualTo(totalSize);
        assertThat(findNews.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(findNews.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(findNews.getNumberOfElements()).isEqualTo(content.size());
    }


    @Test
    public void findPrevByNews_이전_존재하는_뉴스_테스트() {
        //given
        Long id = 2L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();

        Long prevId = 1L;
        List<News> newsList = new ArrayList<>();
        newsList.add(News.builder()
                .id(prevId)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build());
        PageRequest pageRequest = PageRequest.of(0, 1);
        when(newsRepository.findPrevByIdNotDeleted(id, pageRequest)).thenReturn(newsList);

        ///when
        Optional<News> findNews = newsService.findPrevByNews(curNews);

        //then
        assertThat(findNews).isNotEmpty();
        assertThat(findNews.get().getId()).isEqualTo(prevId);
        verify(newsRepository, times(1)).findPrevByIdNotDeleted(id, pageRequest);
    }

    @Test
    public void findPrevByNews_이전_존재하지_않는_뉴스_테스트() {
        //given
        Long id = 1L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();

        PageRequest pageRequest = PageRequest.of(0, 1);
        when(newsRepository.findPrevByIdNotDeleted(id, pageRequest)).thenReturn(List.of());

        ///when
        Optional<News> findNews = newsService.findPrevByNews(curNews);

        //then
        assertThat(findNews).isEmpty();
        verify(newsRepository, times(1)).findPrevByIdNotDeleted(id, pageRequest);
    }

    @Test
    public void findNextByNews_이전_존재하는_뉴스_테스트() {
        //given
        Long id = 2L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();

        Long nextId = 3L;
        List<News> newsList = new ArrayList<>();
        newsList.add(News.builder()
                .id(nextId)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build());
        PageRequest pageRequest = PageRequest.of(0, 1);
        when(newsRepository.findNextByIdNotDeleted(id, pageRequest)).thenReturn(newsList);

        ///when
        Optional<News> findNews = newsService.findNextByNews(curNews);

        //then
        assertThat(findNews).isNotEmpty();
        assertThat(findNews.get().getId()).isEqualTo(nextId);
        verify(newsRepository, times(1)).findNextByIdNotDeleted(id, pageRequest);
    }

    @Test
    public void findNextByNews_이전_존재하지_않는_뉴스_테스트() {
        //given
        Long id = 2L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();

        PageRequest pageRequest = PageRequest.of(0, 1);
        when(newsRepository.findNextByIdNotDeleted(id, pageRequest)).thenReturn(List.of());

        ///when
        Optional<News> findNews = newsService.findNextByNews(curNews);

        //then
        assertThat(findNews).isEmpty();
        verify(newsRepository, times(1)).findNextByIdNotDeleted(id, pageRequest);
    }

}