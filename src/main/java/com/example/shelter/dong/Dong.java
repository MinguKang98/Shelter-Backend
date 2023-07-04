package com.example.shelter.dong;

import com.example.shelter.sigungu.Sigungu;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "dongs")
@NoArgsConstructor
public class Dong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dong_id")
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "sigungu_id")
    private Sigungu sigungu;

    @Column
    private boolean isDeleted = false;

    @Builder
    public Dong(Long id, String name, Sigungu sigungu) {
        this.id = id;
        this.name = name;
        this.sigungu = sigungu;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSigungu(Sigungu sigungu) {
        this.sigungu = sigungu;
    }

    public void updateDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
