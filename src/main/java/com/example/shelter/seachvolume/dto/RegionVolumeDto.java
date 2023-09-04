package com.example.shelter.seachvolume.dto;

import com.example.shelter.shelter.ShelterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionVolumeDto {

    private ShelterType shelterType;
    private String name;
    private Long count;

    public RegionVolumeDto(ShelterType shelterType, String name, Long count) {
        this.shelterType = shelterType;
        this.name = name;
        this.count = count;
    }

}
