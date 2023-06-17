package com.example.shelter.sido;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SidoRepository extends JpaRepository<Sido, Long> {

    /**
     * 특정 이름을 가진 Sido 를 반환
     *
     * @param name 찾는 Sido 의 이름
     * @return 특정 이름을 가진 Sido. 없다면 Optional.empty()
     */
    Optional<Sido> findByName(String name);

}
