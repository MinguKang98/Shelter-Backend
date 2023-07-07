package com.example.shelter.dong;

import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DongRepositoryTest {

    @Autowired
    DongRepository dongRepository;

    @Autowired
    TestEntityManager em;

    @Test
    public void save_테스트() {
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

        ///when
        Dong saveDong = dongRepository.save(dong);
        em.flush();
        em.clear();

        //then
        Dong findDong = em.find(Dong.class, saveDong.getId());
        assertThat(findDong.getId()).isEqualTo(saveDong.getId());
    }

    @Test
    public void findByIdNotDeleted_존재하는_동_테스트() {
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
        em.persistAndFlush(dong);

        ///when
        Optional<Dong> findDong = dongRepository.findByIdNotDeleted(dong.getId());

        //then
        assertThat(findDong).isNotEmpty();
        assertThat(findDong.get().getId()).isEqualTo(dong.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_동_테스트() {
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
        dong.updateDeleted(true);
        em.persistAndFlush(dong);

        ///when
        Optional<Dong> findDong = dongRepository.findByIdNotDeleted(dong.getId());

        //then
        assertThat(findDong).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_동_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<Dong> findDong = dongRepository.findByIdNotDeleted(id);

        //then
        assertThat(findDong).isEmpty();
    }

    @Test
    public void findAllNotDeleted_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        for (int i = 0; i < count; i++) {
            Dong dong = Dong.builder()
                    .name("dong" + i)
                    .sigungu(sigungu)
                    .build();
            em.persist(dong);
        }
        em.flush();

        ///when
        List<Dong> dongs = dongRepository.findAllNotDeleted();

        //then
        assertThat(dongs.size()).isEqualTo(count);
    }

    @Test
    public void findAllNotDeleted_Sort_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        for (int i = 0; i < count; i++) {
            Dong dong = Dong.builder()
                    .name("dong" + i)
                    .sigungu(sigungu)
                    .build();
            em.persist(dong);
        }
        em.flush();

        ///when
        List<Dong> dongs = dongRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));

        //then
        assertThat(dongs.size()).isEqualTo(count);
        assertThat(dongs).isSortedAccordingTo(Comparator.comparing(Dong::getName));
    }

    @Test
    public void findAllNotDeleted_삭제된_동_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        for (int i = 0; i < count; i++) {
            Dong dong = Dong.builder()
                    .name("dong" + i)
                    .sigungu(sigungu)
                    .build();
            dong.updateDeleted(true);
            em.persist(dong);
        }
        em.flush();

        ///when
        List<Dong> dongs = dongRepository.findAllNotDeleted();

        //then
        assertThat(dongs.size()).isEqualTo(0);
    }

    @Test
    public void findAllBySigunguNotDeleted_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        for (int i = 0; i < count; i++) {
            Dong dong = Dong.builder()
                    .name("dong" + i)
                    .sigungu(sigungu)
                    .build();
            em.persist(dong);
        }
        em.flush();

        ///when
        List<Dong> dongs = dongRepository.findAllBySigunguNotDeleted(sigungu);

        //then
        assertThat(dongs.size()).isEqualTo(count);
    }

    @Test
    public void findAllBySigunguNotDeleted_Sort_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        for (int i = 0; i < count; i++) {
            Dong dong = Dong.builder()
                    .name("dong" + i)
                    .sigungu(sigungu)
                    .build();
            em.persist(dong);
        }
        em.flush();

        ///when
        List<Dong> dongs = dongRepository.findAllBySigunguNotDeleted(sigungu,
                Sort.by(Sort.Direction.ASC, "name"));

        //then
        assertThat(dongs.size()).isEqualTo(count);
        assertThat(dongs).isSortedAccordingTo(Comparator.comparing(Dong::getName));
    }

    @Test
    public void findAllBySigunguNotDeleted_삭제된_동_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        for (int i = 0; i < count; i++) {
            Dong dong = Dong.builder()
                    .name("dong" + i)
                    .sigungu(sigungu)
                    .build();
            dong.updateDeleted(true);
            em.persist(dong);
        }
        em.flush();

        ///when
        List<Dong> dongs = dongRepository.findAllBySigunguNotDeleted(sigungu);

        //then
        assertThat(dongs.size()).isEqualTo(0);
    }

    @Test
    public void delete_테스트() {
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
        dong.updateDeleted(true);
        em.persistAndFlush(dong);
        em.clear();

        ///when
        dongRepository.delete(dong);

        //then
        Dong findDong = em.find(Dong.class, dong.getId());
        assertThat(findDong).isNull();
    }

}