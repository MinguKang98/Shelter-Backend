package com.example.shelter.earthquake;

import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.shelter.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "earthquake_shelters")
@NoArgsConstructor
@DiscriminatorValue("earthquake")
public class EarthquakeShelter extends Shelter {

    @Column
    private int area; // 시설면적

    public EarthquakeShelter(Long id, String name, Address address, Double latitude,
                             Double longitude, Dong dong, int area) {
        super(id, name, address, latitude, longitude, dong);
        this.area = area;
    }

    @Override
    public ShelterType getShelterType() {
        return ShelterType.EARTHQUAKE;
    }

}

