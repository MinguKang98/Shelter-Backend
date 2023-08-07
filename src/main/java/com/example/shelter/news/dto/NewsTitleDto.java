package com.example.shelter.news.dto;

import com.example.shelter.news.News;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsTitleDto {

    private Long id;

    private String title;

    protected NewsTitleDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    protected NewsTitleDto(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
    }

    public static NewsTitleDto empty() {
        return new NewsTitleDto(0L, "");
    }

    public static NewsTitleDto of(News news) {
        return new NewsTitleDto(news);
    }

}
