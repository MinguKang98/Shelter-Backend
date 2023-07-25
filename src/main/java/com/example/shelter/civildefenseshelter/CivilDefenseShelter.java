package com.example.shelter.civildefenseshelter;

import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.shelter.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "civil_defense_shelters")
@NoArgsConstructor
@DiscriminatorValue("civil_defense")
public class CivilDefenseShelter extends Shelter {

    @Column
    private int area; // 시설면적

    @Column
    private String type; // 시설 구분명

    @Builder
    public CivilDefenseShelter(Long id, String name, Address address, Double latitude, Double longitude,
                               Dong dong, int area, String type) {
        super(id, name, address, latitude, longitude, dong);
        this.area = area;
        this.type = type;
    }

    @Override
    public ShelterType getShelterType() {
        return ShelterType.CIVIL_DEFENCE;
    }

}
