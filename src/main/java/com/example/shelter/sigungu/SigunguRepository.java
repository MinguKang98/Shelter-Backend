package com.example.shelter.sigungu;

import com.example.shelter.sido.Sido;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {

    List<Sigungu> findAllBySido(Sido sido, Sort sort);

}
