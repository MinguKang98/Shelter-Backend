package com.example.shelter.tsunamishelter;

import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.shelter.address.Address;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tsunami_shelters")
@NoArgsConstructor
@DiscriminatorValue("tsunami")
public class TsunamiShelter extends Shelter {

    @Column
    private int capacity; // 수용가능 인원

    @Column
    private int length; // 해변으로부터 거리

    @Column
    private int height; // 해발 높이

    @Column
    private String type; // 대피소 분류명

    @Builder
    public TsunamiShelter(Long id, String name, Address address, Double latitude, Double longitude,
                          Dong dong, int capacity, int length, int height, String type) {
        super(id, name, address, latitude, longitude, dong);
        this.capacity = capacity;
        this.length = length;
        this.height = height;
        this.type = type;
    }

    @Override
    public ShelterType getShelterType() {
        return ShelterType.TSUNAMI;
    }

}
