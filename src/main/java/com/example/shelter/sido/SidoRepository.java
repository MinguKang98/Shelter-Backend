package com.example.shelter.sido;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SidoRepository extends JpaRepository<Sido, Long> {

    @Query("select s from Sido s where s.id = :id and s.isDeleted = false")
    Optional<Sido> findByIdNotDeleted(@Param("id") Long id);

    /**
     * 특정 이름을 가진 Sido 를 반환
     *
     * @param name 찾는 Sido 의 이름
     * @return 특정 이름을 가진 Sido. 없다면 Optional.empty()
     */
    @Query("select s from Sido s where s.name = :name and s.isDeleted = false")
    Optional<Sido> findByNameNotDeleted(@Param("name") String name);

    @Query("select s from Sido s where s.isDeleted = false")
    List<Sido> findAllNotDeleted();

    @Query("select s from Sido s where s.isDeleted = false")
    List<Sido> findAllNotDeleted(Sort sort);

}
