package com.example.shelter.seachvolume.dto;

import com.example.shelter.seachvolume.SearchVolume;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ShelterTypeVolumeDto {

    private Map<String, Long> tsunami = new LinkedHashMap<>();

    private Map<String, Long> earthquake = new LinkedHashMap<>();

    private Map<String, Long> civilDefense = new LinkedHashMap<>();

    @JsonIgnore
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static ShelterTypeVolumeDto ofRegionVolume(List<RegionVolumeDto> dtoList, List<String> regionList) {

        ShelterTypeVolumeDto volumeDto = new ShelterTypeVolumeDto();
        for (String region : regionList) {
            volumeDto.getTsunami().put(region, 0L);
            volumeDto.getEarthquake().put(region, 0L);
            volumeDto.getCivilDefense().put(region, 0L);
        }

        for (RegionVolumeDto dto : dtoList) {
            switch (dto.getShelterType()) {
                case TSUNAMI -> volumeDto.getTsunami().put(dto.getName(), dto.getCount());
                case EARTHQUAKE -> volumeDto.getEarthquake().put(dto.getName(), dto.getCount());
                case CIVIL_DEFENCE -> volumeDto.getCivilDefense().put(dto.getName(), dto.getCount());
            }
        }

        return volumeDto;
    }

    public static ShelterTypeVolumeDto ofSearchVolume(List<SearchVolume> searchVolumeList, List<String> dateList) {

        ShelterTypeVolumeDto volumeDto = new ShelterTypeVolumeDto();
        for (String date : dateList) {
            volumeDto.getTsunami().put(date, 0L);
            volumeDto.getEarthquake().put(date, 0L);
            volumeDto.getCivilDefense().put(date, 0L);
        }

        for (SearchVolume searchVolume : searchVolumeList) {
            String date = searchVolume.getCreatedDate().format(formatter);
            switch (searchVolume.getShelterType()) {
                case TSUNAMI -> volumeDto.getTsunami().put(date, (long) searchVolume.getVolume());
                case EARTHQUAKE -> volumeDto.getEarthquake().put(date, (long) searchVolume.getVolume());
                case CIVIL_DEFENCE -> volumeDto.getCivilDefense().put(date, (long) searchVolume.getVolume());
            }
        }

        return volumeDto;
    }



}
