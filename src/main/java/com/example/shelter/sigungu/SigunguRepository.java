package com.example.shelter.sigungu;

import com.example.shelter.sido.Sido;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {

    @Query("select s from Sigungu s where s.id = :id and s.isDeleted = false")
    Optional<Sigungu> findByIdNotDeleted(@Param("id") Long id);

    @Query("select s from Sigungu s where s.isDeleted = false")
    List<Sigungu> findAllNotDeleted();

    @Query("select s from Sigungu s where s.isDeleted = false")
    List<Sigungu> findAllNotDeleted(Sort sort);

    @Query("select s from Sigungu s where s.sido = :sido and s.isDeleted = false")
    List<Sigungu> findAllBySidoNotDeleted(@Param("sido") Sido sido);

    @Query("select s from Sigungu s where s.sido = :sido and s.isDeleted = false")
    List<Sigungu> findAllBySidoNotDeleted(@Param("sido") Sido sido, Sort sort);

    @Query("select s from Sigungu s where s.sido = :sido and s.name = :name and s.isDeleted = false")
    Optional<Sigungu> findBySidoAndNameNotDeleted(@Param("sido") Sido sido, @Param("name") String name);

}
