package com.example.shelter.shelter.repository;

import com.example.shelter.shelter.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    @Query("""
            select s from Shelter s
            where s.latitude between :minLat and :maxLat and
            s.longitude between :minLon and :maxLon and
            s.isDeleted = false
            """)
    List<Shelter> findAllBySquareRangeNotDeleted(@Param("minLat") double minLat,
                                                 @Param("maxLat") double maxLat,
                                                 @Param("minLon") double minLon,
                                                 @Param("maxLon") double maxLon);

}
