package com.example.shelter.seachvolume;

import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.ShelterType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Table(name = "search_volumes")
@NoArgsConstructor
public class SearchVolume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_volume_id")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column
    private int volume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @Enumerated(EnumType.STRING)
    private ShelterType shelterType;

    @Column
    private boolean isDeleted = false;

    @Builder
    public SearchVolume(Long id, LocalDate createdDate, LocalDateTime modifiedDate,
                        int volume, Dong dong, ShelterType shelterType) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.volume = volume;
        this.dong = dong;
        this.shelterType = shelterType;
    }

    public void updateDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
