package com.example.shelter.civildefenseshelter.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RawCivilDefenseShelter {

    double area;

    String fullAddress;

    String roadAddress;

    String name;

    String coordType;

    double latitude;

    double longitude;

    String type;

    @Builder
    public RawCivilDefenseShelter(double area, String fullAddress, String roadAddress, String name,
                                  String coordType, double latitude, double longitude, String type) {
        this.area = area;
        this.fullAddress = fullAddress;
        this.roadAddress = roadAddress;
        this.name = name;
        this.coordType = coordType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

}
