package com.example.shelter.sido;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sidos")
@NoArgsConstructor
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sido_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Builder
    public Sido(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }

}
