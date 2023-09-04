package com.example.shelter.tsunamishelter.dto;

import com.example.shelter.tsunamishelter.TsunamiShelter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TsunamiShelterDto {

    private Long id;

    private String name;

    private String fullAddress;

    private String roadAddress;

    private double latitude;

    private double longitude;

    private int capacity;

    private int length;

    private int height;

    private String type;

    private TsunamiShelterDto(TsunamiShelter tsunamiShelter) {
        this.id = tsunamiShelter.getId();
        this.name = tsunamiShelter.getName();
        this.fullAddress = tsunamiShelter.getAddress().getFullAddress();
        this.roadAddress = "";
        this.latitude = tsunamiShelter.getLatitude();
        this.longitude = tsunamiShelter.getLongitude();
        this.capacity = tsunamiShelter.getCapacity();
        this.length = tsunamiShelter.getLength();
        this.height = tsunamiShelter.getHeight();
        this.type = tsunamiShelter.getType();
    }

    private TsunamiShelterDto(TsunamiShelter tsunamiShelter, String roadAddress) {
        this.id = tsunamiShelter.getId();
        this.name = tsunamiShelter.getName();
        this.fullAddress = tsunamiShelter.getAddress().getFullAddress();
        this.roadAddress = roadAddress;
        this.latitude = tsunamiShelter.getLatitude();
        this.longitude = tsunamiShelter.getLongitude();
        this.capacity = tsunamiShelter.getCapacity();
        this.length = tsunamiShelter.getLength();
        this.height = tsunamiShelter.getHeight();
        this.type = tsunamiShelter.getType();
    }

    public static TsunamiShelterDto of(TsunamiShelter tsunamiShelter) {
        return new TsunamiShelterDto(tsunamiShelter);
    }

    public static TsunamiShelterDto of(TsunamiShelter tsunamiShelter, String roadAddress) {
        return new TsunamiShelterDto(tsunamiShelter, roadAddress);
    }

}
