package com.example.shelter.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ListDto<T> {

    private long totalCount;

    private List<T> content;

    public ListDto(List<T> content) {
        this.totalCount = content.size();
        this.content = content;
    }

}
