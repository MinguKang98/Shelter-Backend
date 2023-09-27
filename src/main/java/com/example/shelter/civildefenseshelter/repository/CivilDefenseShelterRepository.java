package com.example.shelter.civildefenseshelter.repository;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.dong.Dong;
import com.example.shelter.sido.Sido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CivilDefenseShelterRepository extends JpaRepository<CivilDefenseShelter, Long> {

    @Query("select c from CivilDefenseShelter c where c.id = :id and c.isDeleted = false")
    Optional<CivilDefenseShelter> findByIdNotDeleted(@Param("id") Long id);

    @Query("select c from CivilDefenseShelter c where c.dong = :dong and c.isDeleted = false")
    List<CivilDefenseShelter> findAllByDongNotDeleted(@Param("dong") Dong dong);

    @Query("select c from CivilDefenseShelter c where c.dong = :dong and c.isDeleted = false")
    Page<CivilDefenseShelter> findAllByDongNotDeleted(@Param("dong") Dong dong, Pageable pageable);

    @Query("select c from CivilDefenseShelter c " +
            "where c.latitude between :minLat and :maxLat and " +
            "c.longitude between :minLon and :maxLon and " +
            "c.isDeleted = false")
    List<CivilDefenseShelter> findAllBySquareRangeNotDeleted(@Param("minLat") double minLat,
                                                             @Param("maxLat") double maxLat,
                                                             @Param("minLon") double minLon,
                                                             @Param("maxLon") double maxLon);

    @Query("select count(c) from CivilDefenseShelter c where c.isDeleted = false")
    int countAllNotDeleted();

    @Query("select count(c) from CivilDefenseShelter c " +
            "left join Dong d on c.dong = d " +
            "left join Sigungu sgg on d.sigungu = sgg " +
            "left join Sido sd on sgg.sido = sd " +
            "where sd = :sido and c.isDeleted = false")
    int countAllBySidoNotDeleted(@Param("sido") Sido sido);

}
