package com.example.shelter.sigungu;

import com.example.shelter.sido.Sido;
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
class SigunguRepositoryTest {

    @Autowired
    SigunguRepository sigunguRepository;

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

        ///when
        Sigungu saveSigungu = sigunguRepository.save(sigungu);
        em.flush();

        //then
        Sigungu findSigungu = em.find(Sigungu.class, saveSigungu.getId());
        assertThat(findSigungu.getId()).isEqualTo(saveSigungu.getId());
    }

    @Test
    public void findByIdNotDeleted_존재하는_시군구_테스트() {
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
        em.flush();

        ///when
        Optional<Sigungu> findSigungu = sigunguRepository.findByIdNotDeleted(sigungu.getId());

        //then
        assertThat(findSigungu).isNotEmpty();
        assertThat(findSigungu.get().getId()).isEqualTo(sigungu.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_시군구_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        sigungu.updateDeleted(true);
        em.persist(sigungu);
        em.flush();

        ///when
        Optional<Sigungu> findSigungu = sigunguRepository.findByIdNotDeleted(sigungu.getId());

        //then
        assertThat(findSigungu).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_시군구_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<Sigungu> findSigungu = sigunguRepository.findByIdNotDeleted(id);

        //then
        assertThat(findSigungu).isEmpty();
    }

    @Test
    public void findAllNotDeleted_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < count; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("sigungu" + i)
                    .sido(sido)
                    .build();
            em.persist(sigungu);
        }
        em.flush();

        ///when
        List<Sigungu> sigungus = sigunguRepository.findAllNotDeleted();

        //then
        assertThat(sigungus.size()).isEqualTo(count);
    }

    @Test
    public void findAllNotDeleted_Sort_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < count; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("sigungu" + i)
                    .sido(sido)
                    .build();
            em.persist(sigungu);
        }
        em.flush();

        ///when
        List<Sigungu> sigungus = sigunguRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));

        //then
        assertThat(sigungus.size()).isEqualTo(count);
        assertThat(sigungus).isSortedAccordingTo(Comparator.comparing(Sigungu::getName));
    }

    @Test
    public void findAllNotDeleted_삭제된_시군구_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < count; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("sigungu" + i)
                    .sido(sido)
                    .build();
            sigungu.updateDeleted(true);
            em.persist(sigungu);
        }
        em.flush();

        ///when
        List<Sigungu> sigungus = sigunguRepository.findAllNotDeleted();

        //then
        assertThat(sigungus).isEmpty();
    }

    @Test
    public void findAllBySidoNotDeleted_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < count; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("sigungu" + i)
                    .sido(sido)
                    .build();
            em.persist(sigungu);
        }
        em.flush();

        ///when
        List<Sigungu> sigungus = sigunguRepository.findAllBySidoNotDeleted(sido);

        //then
        assertThat(sigungus.size()).isEqualTo(count);
    }

    //findAllBySidoNotDeleted_Sort
    @Test
    public void findAllBySidoNotDeleted_Sort_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < count; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("sigungu" + i)
                    .sido(sido)
                    .build();
            em.persist(sigungu);
        }
        em.flush();

        ///when
        List<Sigungu> sigungus = sigunguRepository.findAllBySidoNotDeleted(sido, Sort.by(Sort.Direction.ASC, "name"));

        //then
        assertThat(sigungus.size()).isEqualTo(count);
        assertThat(sigungus).isSortedAccordingTo(Comparator.comparing(Sigungu::getName));
    }

    @Test
    public void findAllBySidoNotDeleted_삭제된_시군구_테스트() {
        //given
        int count = 10;
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < count; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("sigungu" + i)
                    .sido(sido)
                    .build();
            sigungu.updateDeleted(true);
            em.persist(sigungu);
        }
        em.flush();

        ///when
        List<Sigungu> sigungus = sigunguRepository.findAllBySidoNotDeleted(sido);

        //then
        assertThat(sigungus).isEmpty();
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
        em.flush();

        ///when
        sigunguRepository.delete(sigungu);

        //then
        Sigungu findSigungu = em.find(Sigungu.class, sigungu.getId());
        assertThat(findSigungu).isNull();
    }

}