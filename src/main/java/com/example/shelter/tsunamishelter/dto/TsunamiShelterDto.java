package com.example.shelter.tsunamishelter.dto;

import com.example.shelter.shelter.address.Address;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TsunamiShelterDto {

    private Long id;

    private String name;

    private String fullAddress;

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

}
