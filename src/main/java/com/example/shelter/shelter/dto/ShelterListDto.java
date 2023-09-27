package com.example.shelter.shelter.dto;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.civildefenseshelter.dto.CivilDefenseShelterDto;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.dto.EarthquakeShelterDto;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.dto.TsunamiShelterDto;
import com.example.shelter.util.GpsUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ShelterListDto {

    private long totalCount;

    private double avgLatitude;

    private double avgLongitude;

    private List<?> content;

    protected ShelterListDto(List<? extends Shelter> content) {
        this.totalCount = content.size();
        this.content = content.stream()
                .map(s ->
                        switch (s.getShelterType()) {
                            case TSUNAMI -> TsunamiShelterDto.of((TsunamiShelter) s);
                            case EARTHQUAKE -> EarthquakeShelterDto.of((EarthquakeShelter) s);
                            case CIVIL_DEFENCE -> CivilDefenseShelterDto.of((CivilDefenseShelter) s);
                        }
                )
                .collect(Collectors.toList());
        this.avgLatitude = GpsUtils.getAvgLatitude(content);
        this.avgLongitude = GpsUtils.getAvgLongitude(content);
    }

    protected ShelterListDto(List<? extends Shelter> content, List<String> roadAddresses) {
        this.totalCount = content.size();
        this.content = content.stream()
                .map(s -> {
                            int index = content.indexOf(s);
                            return switch (s.getShelterType()) {
                                case TSUNAMI -> TsunamiShelterDto.of((TsunamiShelter) s, roadAddresses.get(index));
                                case EARTHQUAKE -> EarthquakeShelterDto.of((EarthquakeShelter) s, roadAddresses.get(index));
                                case CIVIL_DEFENCE -> CivilDefenseShelterDto.of((CivilDefenseShelter) s);
                            };
                        }
                )
                .collect(Collectors.toList());
        this.avgLatitude = GpsUtils.getAvgLatitude(content);
        this.avgLongitude = GpsUtils.getAvgLongitude(content);
    }

    public static ShelterListDto of(List<? extends Shelter> content) {
        return new ShelterListDto(content);
    }

    public static ShelterListDto of(List<? extends Shelter> content, List<String> roadAddresses) {
        return new ShelterListDto(content, roadAddresses);
    }

}
