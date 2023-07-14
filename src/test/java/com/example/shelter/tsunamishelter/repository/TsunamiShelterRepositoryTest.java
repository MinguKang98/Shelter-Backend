package com.example.shelter.tsunamishelter.repository;

import com.example.shelter.dong.Dong;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TsunamiShelterRepositoryTest {

    @Autowired
    TsunamiShelterRepository tsunamiShelterRepository;

    @Autowired
    TestEntityManager em;

    @Test
    public void findByIdNotDeleted_존재하는_지진해일대피소_테스트() {
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

        TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .capacity(100)
                .height(10)
                .length(10)
                .type("학교")
                .build();
        em.persistAndFlush(tsunamiShelter);
        em.clear();

        ///when
        Optional<TsunamiShelter> findShelter = tsunamiShelterRepository
                .findByIdNotDeleted(tsunamiShelter.getId());

        //then
        assertThat(findShelter).isNotEmpty();
        assertThat(findShelter.get().getId()).isEqualTo(tsunamiShelter.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_지진해일대피소_테스트() {
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

        TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .capacity(100)
                .height(10)
                .length(10)
                .type("학교")
                .build();
        tsunamiShelter.updateDeleted(true);
        em.persistAndFlush(tsunamiShelter);
        em.clear();

        ///when
        Optional<TsunamiShelter> findShelter = tsunamiShelterRepository
                .findByIdNotDeleted(tsunamiShelter.getId());

        //then
        assertThat(findShelter).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_지진해일대피소_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<TsunamiShelter> findShelter = tsunamiShelterRepository.findByIdNotDeleted(id);

        //then
        assertThat(findShelter).isEmpty();
    }

    @Test
    public void findAllByDongNotDeleted_존재하는_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<TsunamiShelter> shelters = tsunamiShelterRepository.findAllByDongNotDeleted(dong, pageRequest);

        //then
        assertThat(shelters.getNumber()).isEqualTo(0);
        assertThat(shelters.getSize()).isEqualTo(4);
        assertThat(shelters.getNumberOfElements()).isEqualTo(4);
    }

    @Test
    public void findAllByDongNotDeleted_삭제된_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            tsunamiShelter.updateDeleted(true);
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<TsunamiShelter> shelters = tsunamiShelterRepository.findAllByDongNotDeleted(dong, pageRequest);

        //then
        assertThat(shelters.getContent().isEmpty()).isTrue();
    }

    @Test
    public void findAllBySquareRangeNotDeleted_존재하는_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", String.valueOf(i)))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        List<TsunamiShelter> shelters = tsunamiShelterRepository
                .findAllBySquareRangeNotDeleted(35, 40, 126, 129);

        //then
        assertThat(shelters.size()).isEqualTo(3);
    }

    @Test
    public void findAllBySquareRangeNotDeleted_삭제된_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", String.valueOf(i)))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            tsunamiShelter.updateDeleted(true);
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        List<TsunamiShelter> shelters = tsunamiShelterRepository
                .findAllBySquareRangeNotDeleted(30, 40, 120, 130);

        //then
        assertThat(shelters.isEmpty()).isTrue();
    }

    @Test
    public void countAll_존재하는_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = tsunamiShelterRepository.countAll();

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAll_삭제된_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            tsunamiShelter.updateDeleted(true);
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = tsunamiShelterRepository.countAll();

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void countAllBySido_시도에_존재하는_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = tsunamiShelterRepository.countAllBySido(sido);

        //then
        assertThat(count).isEqualTo(10);
    }

    @Test
    public void countAllBySido_시도에_존재하지_않는_지진해일대피소_테스트() {
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
            TsunamiShelter tsunamiShelter = TsunamiShelter.builder().name("테스트 대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            em.persist(tsunamiShelter);
        }
        em.flush();
        em.clear();

        ///when
        int count = tsunamiShelterRepository.countAllBySido(sido2);

        //then
        assertThat(count).isEqualTo(0);
    }

}