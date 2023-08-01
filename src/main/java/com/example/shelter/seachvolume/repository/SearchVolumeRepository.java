package com.example.shelter.seachvolume.repository;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SearchVolumeRepository extends JpaRepository<SearchVolume, Long> {

    @Query("select s from SearchVolume s " +
            "where s.dong = :dong and " +
            "s.shelterType = :type and " +
            "s.createdDate = :date and s.isDeleted = false ")
    Optional<SearchVolume> findByDongAndTypeAndDateNotDeleted(@Param("dong") Dong dong,
                                                              @Param("type") ShelterType type,
                                                              @Param("date") LocalDate date);


    @Query("select coalesce(sum(s.volume), 0) from SearchVolume s " +
            "where s.createdDate =:date and s.isDeleted = false")
    int getTotalVolumeByDateNotDeleted(@Param("date") LocalDate date);

    @Query("select coalesce(sum(s.volume), 0) from SearchVolume s " +
            "where s.createdDate between :from and :to and s.isDeleted = false")
    int getTotalVolumeByDateRangeNotDeleted(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("select s from SearchVolume s " +
            "where s.dong = :dong and " +
            "s.shelterType = :type and " +
            "s.createdDate between :from and :to and " +
            "s.isDeleted = false ")
    List<SearchVolume> findAllByDongAndTypeAndDateRangeNotDeleted(@Param("dong") Dong dong,
                                                                  @Param("type") ShelterType type,
                                                                  @Param("from") LocalDate from,
                                                                  @Param("to") LocalDate to);

    @Query("""
            select new com.example.shelter.seachvolume.dto.RegionVolumeDto
            (sd.name, s.shelterType, sum(s.volume))
            from SearchVolume s
            left join Dong d on s.dong = d
            left join Sigungu sgg on d.sigungu = sgg
            left join Sido sd on sgg.sido = sd
            where s.createdDate = :date and
            s.isDeleted = false
            group by sd.id, s.shelterType
            """)
    List<RegionVolumeDto> countSidoByDateNotDeleted(@Param("date") LocalDate date);


    @Query("""
            select new com.example.shelter.seachvolume.dto.RegionVolumeDto
            (sgg.name, s.shelterType, sum(s.volume))
            from SearchVolume s
            left join Dong d on s.dong = d
            left join Sigungu sgg on d.sigungu = sgg
            left join Sido sd on sgg.sido = sd
            where sd = :sido and
            s.createdDate = :date and
            s.isDeleted = false
            group by sgg.id, s.shelterType
            """)
    List<RegionVolumeDto> countSigunguBySidoAndDateNotDeleted(@Param("sido") Sido sido,
                                                              @Param("date") LocalDate date);

}
