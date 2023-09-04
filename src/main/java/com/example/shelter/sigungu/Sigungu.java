package com.example.shelter.sigungu;

import com.example.shelter.common.BaseEntity;
import com.example.shelter.sido.Sido;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sigungus")
@NoArgsConstructor
public class Sigungu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sigungu_id")
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private Sido sido;

    @Column
    private boolean isDeleted = false;

    @Builder
    public Sigungu(Long id, String name, Sido sido) {
        this.id = id;
        this.name = name;
        this.sido = sido;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSido(Sido sido) {
        this.sido = sido;
    }

    public void updateDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
