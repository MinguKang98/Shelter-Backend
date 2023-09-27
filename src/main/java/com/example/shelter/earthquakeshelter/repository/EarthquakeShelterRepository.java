package com.example.shelter.earthquakeshelter.repository;

import com.example.shelter.dong.Dong;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.sido.Sido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EarthquakeShelterRepository extends JpaRepository<EarthquakeShelter, Long> {

    @Query("select e from EarthquakeShelter e where e.id = :id and e.isDeleted = false")
    Optional<EarthquakeShelter> findByIdNotDeleted(@Param("id") Long id);

    @Query("select e from EarthquakeShelter e where e.dong = :dong and e.isDeleted = false")
    List<EarthquakeShelter> findAllByDongNotDeleted(@Param("dong") Dong dong);

    @Query("select e from EarthquakeShelter e where e.dong = :dong and e.isDeleted = false")
    Page<EarthquakeShelter> findAllByDongNotDeleted(@Param("dong") Dong dong, Pageable pageable);

    @Query("select e from EarthquakeShelter e " +
            "where e.latitude between :minLat and :maxLat and " +
            "e.longitude between :minLon and :maxLon and " +
            "e.isDeleted = false")
    List<EarthquakeShelter> findAllBySquareRangeNotDeleted(@Param("minLat") double minLat,
                                                           @Param("maxLat") double maxLat,
                                                           @Param("minLon") double minLon,
                                                           @Param("maxLon") double maxLon);

    @Query("select count(e) from EarthquakeShelter e where e.isDeleted = false")
    int countAllNotDeleted();

    @Query("select count(e) from EarthquakeShelter e " +
            "left join Dong d on e.dong = d " +
            "left join Sigungu sgg on d.sigungu = sgg " +
            "left join Sido sd on sgg.sido = sd " +
            "where sd = :sido and e.isDeleted = false")
    int countAllBySidoNotDeleted(@Param("sido") Sido sido);

}
