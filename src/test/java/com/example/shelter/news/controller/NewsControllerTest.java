package com.example.shelter.news.controller;

import com.example.shelter.exception.notfound.NewsNotFoundException;
import com.example.shelter.news.News;
import com.example.shelter.news.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NewsService newsService;

    @Test
    public void getNews_존재하는_뉴스_테스트() throws Exception {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Long id = 1L;
        News news = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();
        when(newsService.findById(id)).thenReturn(news);

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.publishDate").value(news.getPublishTime().format(formatter)))
                .andExpect(jsonPath("$.writer").value(news.getWriter()))
                .andExpect(jsonPath("$.title").value(news.getTitle()))
                .andExpect(jsonPath("$.content").value(news.getContent()));
    }

    @Test
    public void getNews_존재하지_않는_뉴스_테스트() throws Exception {
        //given
        Long id = 1L;
        when(newsService.findById(id)).thenThrow(new NewsNotFoundException());

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("106"));
    }


    @Test
    public void getNewsPage_테스트() throws Exception {
        //given
        long total = 100L;
        int page = 1;
        int size = 8;
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishTime"));
        List<News> content = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            News news = News.builder()
                    .id((long) i)
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목")
                    .content("내용")
                    .build();
            content.add(news);
        }
        PageImpl<News> result = new PageImpl<>(content, pageRequest, total);
        when(newsService.findAll(pageRequest)).thenReturn(result);

        ///when
        //then
        mockMvc.perform(get("/api/news").param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(result.getTotalElements()))
                .andExpect(jsonPath("$.totalPage").value(result.getTotalPages()))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.hasNext").value(result.hasNext()))
                .andExpect(jsonPath("$.hasPrevious").value(result.hasPrevious()));
    }


    @Test
    public void getPrevNews_다음_존재하는_뉴스_테스트() throws Exception {
        //given
        Long id = 3L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목3")
                .content("내용3")
                .build();

        Long prevId = 2L;
        News prevNews = News.builder()
                .id(prevId)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목2")
                .content("내용2")
                .build();

        when(newsService.findById(id)).thenReturn(curNews);
        when(newsService.findPrevByNews(curNews)).thenReturn(Optional.of(prevNews));

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}/prev", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(prevNews.getId()))
                .andExpect(jsonPath("$.title").value(prevNews.getTitle()));
    }

    @Test
    public void getPrevNews_다음_존재하지_않는_뉴스_테스트() throws Exception {
        //given
        Long id = 1L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목1")
                .content("내용1")
                .build();


        when(newsService.findById(id)).thenReturn(curNews);
        when(newsService.findPrevByNews(curNews)).thenReturn(Optional.empty());

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}/prev", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.title").value(""));
    }

    @Test
    public void getPrevNews_존재하지_않는_뉴스_테스트() throws Exception {
        //given
        Long id = 1L;

        when(newsService.findById(id)).thenThrow(new NewsNotFoundException());

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("106"));
    }

    @Test
    public void getNextNews_다음_존재하는_뉴스_테스트() throws Exception {
        //given
        Long id = 3L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목3")
                .content("내용3")
                .build();

        Long nextId = 4L;
        News nextNews = News.builder()
                .id(nextId)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목4")
                .content("내용4")
                .build();

        when(newsService.findById(id)).thenReturn(curNews);
        when(newsService.findNextByNews(curNews)).thenReturn(Optional.of(nextNews));

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}/next", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(nextNews.getId()))
                .andExpect(jsonPath("$.title").value(nextNews.getTitle()));
    }

    @Test
    public void getNextNews_다음_존재하지_않는_뉴스_테스트() throws Exception {
        //given
        Long id = 1L;
        News curNews = News.builder()
                .id(id)
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목1")
                .content("내용1")
                .build();


        when(newsService.findById(id)).thenReturn(curNews);
        when(newsService.findNextByNews(curNews)).thenReturn(Optional.empty());

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}/next", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.title").value(""));
    }

    @Test
    public void getNextNews_존재하지_않는_뉴스_테스트() throws Exception {
        //given
        Long id = 1L;

        when(newsService.findById(id)).thenThrow(new NewsNotFoundException());

        ///when
        //then
        mockMvc.perform(get("/api/news/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("106"));
    }

}