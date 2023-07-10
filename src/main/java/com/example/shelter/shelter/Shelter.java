package com.example.shelter.shelter;

import com.example.shelter.common.BaseEntity;
import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shelters")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Shelter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Address address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @Column
    private boolean isDeleted = false;

    public Shelter(Long id, String name, Address address, Double latitude, Double longitude, Dong dong) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dong = dong;
    }

    public abstract ShelterType getShelterType();

}
