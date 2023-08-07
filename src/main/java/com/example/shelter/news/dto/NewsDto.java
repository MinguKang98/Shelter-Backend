package com.example.shelter.news.dto;

import com.example.shelter.news.News;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class NewsDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    private String writer;

    private String title;

    private String content;

    protected NewsDto(News news) {
        this.id = news.getId();
        this.publishDate = news.getPublishTime().toLocalDate();
        this.writer = news.getWriter();
        this.title = news.getTitle();
        this.content = news.getContent();
    }

    public static NewsDto of(News news) {
        return new NewsDto(news);
    }

}
