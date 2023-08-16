package com.example.shelter.seachvolume.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TotalDto {

    int totalCount;

    protected TotalDto(int totalCount) {
        this.totalCount = totalCount;
    }

    public static TotalDto of(int totalCount) {
        return new TotalDto(totalCount);
    }

}
