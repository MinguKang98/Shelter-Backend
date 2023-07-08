package com.example.shelter.tsunamishelter;

import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.ShelterType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tsunami_shelters")
@NoArgsConstructor
@DiscriminatorColumn(name = "tsunami")
public class TsunamiShelter extends Shelter {

    @Column
    private Long capacity; // 수용가능 인원

    @Column
    private Long length; // 해변으로부터 거리

    @Column
    private Long height; // 해발 높이

    @Column
    private String type; // 대피소 분류명

    @Builder
    public TsunamiShelter(Long id, String name, Double latitude, Double longitude, Dong dong,
                          Long capacity, Long length, Long height, String type) {
        super(id, name, latitude, longitude, dong);
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
