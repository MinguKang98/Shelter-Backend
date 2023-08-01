package com.example.shelter.seachvolume.dto;

import com.example.shelter.shelter.ShelterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionVolumeDto {

    private String name;
    private ShelterType shelterType;
    private Long count;

    public RegionVolumeDto(String name, ShelterType shelterType, Long count) {
        this.name = name;
        this.shelterType = shelterType;
        this.count = count;
    }

}
