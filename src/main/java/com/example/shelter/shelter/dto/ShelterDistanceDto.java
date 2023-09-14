package com.example.shelter.shelter.dto;

import com.example.shelter.shelter.Shelter;
import com.example.shelter.util.GpsUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShelterDistanceDto {

    private Long id;

    private String name;

    private double latitude;

    private double longitude;

    private int distance;

    private String type;

    protected ShelterDistanceDto(Shelter shelter, double curLat, double curLon) {
        this.id = shelter.getId();
        this.name = shelter.getName();
        this.latitude = shelter.getLatitude();
        this.longitude = shelter.getLongitude();
        this.distance = (int) GpsUtils.getDistance(curLat, curLon, shelter.getLatitude(), shelter.getLongitude());
        this.type = shelter.getShelterType().getName();

    }

    public static ShelterDistanceDto of(Shelter shelter, double curLat, double curLon) {
        return new ShelterDistanceDto(shelter, curLat, curLon);
    }

}
