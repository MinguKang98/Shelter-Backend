package com.example.shelter.dong;

import com.example.shelter.sigungu.Sigungu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DongRepository extends JpaRepository<Dong, Long> {

    @Query("select d from Dong d where d.id = :id and d.isDeleted = false")
    Optional<Dong> findByIdNotDeleted(@Param("id") Long id);

    @Query("select d from Dong d where d.isDeleted = false")
    List<Dong> findAllNotDeleted();

    @Query("select d from Dong d where d.isDeleted = false")
    List<Dong> findAllNotDeleted(Sort sort);

    @Query("select d from Dong d where d.sigungu = :sigungu and d.isDeleted = false")
    List<Dong> findAllBySigunguNotDeleted(@Param("sigungu") Sigungu sigungu);

    @Query("select d from Dong d where d.sigungu = :sigungu and d.isDeleted = false")
    List<Dong> findAllBySigunguNotDeleted(@Param("sigungu") Sigungu sigungu, Sort sort);

}
