package com.example.shelter.news.dto;

import com.example.shelter.news.News;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SimpleNewsDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    private String writer;

    private String title;


    protected SimpleNewsDto(News news) {
        this.id = news.getId();
        this.publishDate = news.getPublishTime().toLocalDate();
        this.writer = news.getWriter();
        this.title = news.getTitle();
    }

    public static SimpleNewsDto of(News news) {
        return new SimpleNewsDto(news);
    }

}
