package com.example.shelter.civildefenseshelter.repository;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CivilDefenseShelterRepositoryTest {

    @Autowired
    CivilDefenseShelterRepository civilDefenseShelterRepository;

    @Autowired
    TestEntityManager em;

    @Test
    public void findByIdNotDeleted_존재하는_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                .name("전일중학교")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .area(100)
                .type("공공시설")
                .build();
        em.persistAndFlush(civilDefenseShelter);
        em.clear();

        ///when
        Optional<CivilDefenseShelter> findShelter = civilDefenseShelterRepository
                .findByIdNotDeleted(civilDefenseShelter.getId());

        //then
        assertThat(findShelter).isNotEmpty();
        assertThat(findShelter.get().getId()).isEqualTo(civilDefenseShelter.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                .name("전일중학교")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .area(100)
                .type("공공시설")
                .build();
        civilDefenseShelter.updateDeleted(true);
        em.persistAndFlush(civilDefenseShelter);
        em.clear();

        ///when
        Optional<CivilDefenseShelter> findShelter = civilDefenseShelterRepository
                .findByIdNotDeleted(civilDefenseShelter.getId());

        //then
        assertThat(findShelter).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_민방위대피소_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<CivilDefenseShelter> findShelter = civilDefenseShelterRepository
                .findByIdNotDeleted(id);

        //then
        assertThat(findShelter).isEmpty();
    }

    @Test
    public void findAllByDongNotDeleted_존재하는_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<CivilDefenseShelter> findShelters = civilDefenseShelterRepository
                .findAllByDongNotDeleted(dong, pageRequest);

        //then
        assertThat(findShelters.getNumber()).isEqualTo(0);
        assertThat(findShelters.getSize()).isEqualTo(4);
        assertThat(findShelters.getNumberOfElements()).isEqualTo(4);
    }

    @Test
    public void findAllByDongNotDeleted_삭제된_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            civilDefenseShelter.updateDeleted(true);
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<CivilDefenseShelter> findShelters = civilDefenseShelterRepository
                .findAllByDongNotDeleted(dong, pageRequest);

        //then
        assertThat(findShelters.isEmpty()).isTrue();
    }

    @Test
    public void findAllBySquareRangeNotDeleted_존재하는_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        List<Double[]> gpsList = List.of(
                new Double[]{34.1, 124.1},
                new Double[]{35.1, 125.1},
                new Double[]{36.1, 126.1},
                new Double[]{37.1, 127.1},
                new Double[]{38.1, 128.1},
                new Double[]{39.1, 129.1}
        );

        for (int i = 0; i < 6; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        List<CivilDefenseShelter> findShelters = civilDefenseShelterRepository
                .findAllBySquareRangeNotDeleted(35, 40, 126, 129);

        //then
        assertThat(findShelters.size()).isEqualTo(3);
    }

    @Test
    public void findAllBySquareRangeNotDeleted_삭제된_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        List<Double[]> gpsList = List.of(
                new Double[]{34.1, 124.1},
                new Double[]{35.1, 125.1},
                new Double[]{36.1, 126.1},
                new Double[]{37.1, 127.1},
                new Double[]{38.1, 128.1},
                new Double[]{39.1, 129.1}
        );

        for (int i = 0; i < 6; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            civilDefenseShelter.updateDeleted(true);
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        List<CivilDefenseShelter> findShelters = civilDefenseShelterRepository
                .findAllBySquareRangeNotDeleted(35, 40, 126, 129);

        //then
        assertThat(findShelters.isEmpty()).isTrue();
    }

    @Test
    public void countAllNotDeleted_존재하는_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = civilDefenseShelterRepository.countAllNotDeleted();

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllNotDeleted_삭제된_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = civilDefenseShelterRepository.countAllNotDeleted();

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllBySidoNotDeleted_시도에_존재하는_민방위대피소_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = civilDefenseShelterRepository.countAllBySidoNotDeleted(sido);

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllBySidoNotDeleted_시도에_존재하지_않는_민방위대피소_테스트() {
        //given
        Sido sido1 = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido1);

        Sido sido2 = Sido.builder()
                .name("경기도")
                .build();
        em.persist(sido2);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido1)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            CivilDefenseShelter civilDefenseShelter = CivilDefenseShelter.builder()
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .type("공공시설")
                    .build();
            em.persist(civilDefenseShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = civilDefenseShelterRepository.countAllBySidoNotDeleted(sido2);

        //then
        assertThat(count).isZero();
    }

}