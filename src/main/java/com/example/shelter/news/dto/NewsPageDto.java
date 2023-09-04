package com.example.shelter.news.dto;

import com.example.shelter.news.News;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class NewsPageDto {

    private long totalCount;

    private int totalPage;

    private int page;

    private int size;

    boolean hasNext;

    boolean hasPrevious;

    private List<SimpleNewsDto> content;

    protected NewsPageDto(Page<News> page) {
        this.totalCount = page.getTotalElements();
        this.totalPage = page.getTotalPages();
        this.page = page.getNumber() + 1;
        this.size = page.getNumberOfElements();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.content = page.getContent().stream()
                .map(SimpleNewsDto::of)
                .collect(Collectors.toList());
    }

    public static NewsPageDto of(Page<News> page) {
        return new NewsPageDto(page);
    }

}
