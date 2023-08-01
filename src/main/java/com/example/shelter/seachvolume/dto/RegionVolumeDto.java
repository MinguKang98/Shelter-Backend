package com.example.shelter.seachvolume.dto;

import com.example.shelter.shelter.ShelterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionVolumeDto {

    private String sidoName;
    private ShelterType shelterType;
    private Long count;

    public RegionVolumeDto(String sidoName, ShelterType shelterType, Long count) {
        this.sidoName = sidoName;
        this.shelterType = shelterType;
        this.count = count;
    }

}
