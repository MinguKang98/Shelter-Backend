package com.example.shelter.sido;

import com.example.shelter.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sidos")
@NoArgsConstructor
public class Sido extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sido_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private boolean isDeleted = false;

    @Builder
    public Sido(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
