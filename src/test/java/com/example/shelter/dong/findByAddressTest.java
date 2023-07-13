package com.example.shelter.dong;

import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoRepository;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.sigungu.SigunguRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class findByAddressTest {

    @Autowired
    DongRepository dongRepository;

    @Autowired
    SigunguRepository sigunguRepository;

    @Autowired
    SidoRepository sidoRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void findByAddress_속도비교_테스트() {
        //given
        String sidoName = "서울특별시";
        String sigunguName = "동대문구";
        String dongName = "전농동";

        //when
        long start1 = System.currentTimeMillis();
        Optional<Dong> findDong = dongRepository.findByAddressNames(sidoName, sigunguName, dongName);
        long end1 = System.currentTimeMillis();
        long time1 = end1 - start1;

        entityManager.clear();

        long start2 = System.currentTimeMillis();
        Optional<Sido> sido = sidoRepository.findByNameNotDeleted(sidoName);
        Optional<Sigungu> sigungu = sigunguRepository.findBySidoAndNameNotDeleted(sido.get(), sigunguName);
        Optional<Dong> findDong2 = dongRepository.findBySigunguAndNameNotDeleted(sigungu.get(), dongName);
        long end2 = System.currentTimeMillis();
        long time2 = end2 - start2;

        //then
        System.out.println("time1 = " + time1);
        System.out.println("time2 = " + time2);

    }

}