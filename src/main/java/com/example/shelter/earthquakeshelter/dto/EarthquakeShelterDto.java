package com.example.shelter.earthquakeshelter.dto;

import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EarthquakeShelterDto {

    private Long id;

    private String name;

    private String fullAddress;

    private String roadAddress;

    private double latitude;

    private double longitude;

    private int area;

    protected EarthquakeShelterDto(EarthquakeShelter earthquakeShelter) {
        this.id = earthquakeShelter.getId();
        this.name = earthquakeShelter.getName();
        this.fullAddress = earthquakeShelter.getAddress().getFullAddress();
        this.roadAddress = "";
        this.latitude = earthquakeShelter.getLatitude();
        this.longitude = earthquakeShelter.getLongitude();
        this.area = earthquakeShelter.getArea();
    }

    protected EarthquakeShelterDto(EarthquakeShelter earthquakeShelter, String roadAddress) {
        this.id = earthquakeShelter.getId();
        this.name = earthquakeShelter.getName();
        this.fullAddress = earthquakeShelter.getAddress().getFullAddress();
        this.roadAddress = roadAddress;
        this.latitude = earthquakeShelter.getLatitude();
        this.longitude = earthquakeShelter.getLongitude();
        this.area = earthquakeShelter.getArea();
    }

    public static EarthquakeShelterDto of(EarthquakeShelter earthquakeShelter) {
        return new EarthquakeShelterDto(earthquakeShelter);
    }

    public static EarthquakeShelterDto of(EarthquakeShelter earthquakeShelter, String roadAddress) {
        return new EarthquakeShelterDto(earthquakeShelter, roadAddress);
    }

}
