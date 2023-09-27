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

    @Query("select d from Dong d where d.sigungu = :sigungu and d.name = :name and d.isDeleted = false")
    Optional<Dong> findBySigunguAndNameNotDeleted(@Param("sigungu") Sigungu sigungu, @Param("name") String name);

    @Query("""
            select d from Dong d
            join fetch d.sigungu sgg
            join fetch sgg.sido sd
            where sd.name = :sidoName and sd.isDeleted = false and
            sgg.name = :sigunguName and sgg.isDeleted = false and
            d.name = :dongName and d.isDeleted = false
            """)
    Optional<Dong> findByAddressNames(@Param("sidoName") String sidoName,
                                      @Param("sigunguName") String sigunguName,
                                      @Param("dongName") String dongName);

}
