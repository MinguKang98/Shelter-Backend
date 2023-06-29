package com.example.shelter.sido;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SidoRepositoryTest {

    @Autowired
    SidoRepository sidoRepository;

    @Autowired
    TestEntityManager em;

    @Test
    public void save_테스트() {
        //given
        String name = "서울시";

        ///when
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());
        em.flush();
        em.clear();

        //then
        Sido findSido = em.find(Sido.class, saveSido.getId());
        assertThat(findSido).isNotNull();
        assertThat(findSido.getId()).isEqualTo(saveSido.getId());
    }

    @Test
    public void findByIdNotDeleted_존재하는_시도_테스트() {
        //given
        String name = "서울시";
        Sido sido = Sido.builder()
                .name(name)
                .build();
        em.persist(sido);
        em.flush();

        ///when
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(sido.getId());

        //then
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getId()).isEqualTo(sido.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_시도_테스트() {
        //given
        String name = "서울시";
        Sido sido = Sido.builder()
                .name(name)
                .build();
        sido.updateDeleted(true);
        em.persist(sido);
        em.flush();

        ///when
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(sido.getId());

        //then
        assertThat(findSido).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_시도_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(id);

        //then
        assertThat(findSido).isEmpty();
    }

    @Test
    public void findByNameNotDeleted_존재하는_시도_테스트() {
        //given
        String name = "서울시";
        Sido sido = Sido.builder()
                .name(name)
                .build();
        em.persistAndFlush(sido);

        ///when
        Optional<Sido> findSido = sidoRepository.findByNameNotDeleted(name);

        //then
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getId()).isEqualTo(sido.getId());
    }

    @Test
    public void findByNameNotDeleted_삭제된_시도_테스트() {
        //given
        String name = "서울시";
        Sido sido = Sido.builder()
                .name(name)
                .build();
        sido.updateDeleted(true);
        em.persistAndFlush(sido);

        ///when
        Optional<Sido> findSido = sidoRepository.findByNameNotDeleted(name);

        //then
        assertThat(findSido).isEmpty();
    }

    @Test
    public void findByNameNotDeleted_존재하지_않는_시도_테스트() {
        //given
        String name = "서울시";

        ///when
        Optional<Sido> findSido = sidoRepository.findByNameNotDeleted(name);

        //then
        assertThat(findSido).isEmpty();
    }

    @Test
    public void findAllNotDeleted_테스트() {
        //given
        int count = 10;
        for (int i = 1; i < count + 1; i++) {
            Sido sido = Sido.builder()
                    .name("sido"+i)
                    .build();
            em.persist(sido);
        }
        em.flush();

        ///when
        List<Sido> sidos = sidoRepository.findAllNotDeleted();

        //then
        assertThat(sidos.size()).isEqualTo(count);
    }

    @Test
    public void findAllNotDeleted_Sort_테스트() {
        //given
        int count = 10;
        for (int i = 1; i < count + 1; i++) {
            Sido sido = Sido.builder()
                    .name("sido"+i)
                    .build();
            em.persist(sido);
        }
        em.flush();

        ///when
        List<Sido> sidos = sidoRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));

        //then
        assertThat(sidos.size()).isEqualTo(count);
        assertThat(sidos).isSortedAccordingTo(Comparator.comparing(Sido::getName));
    }

    @Test
    public void findAllNotDeleted_삭제된_시도_테스트() {
        //given
        int count = 10;
        for (int i = 1; i < count + 1; i++) {
            Sido sido = Sido.builder()
                    .name("sido"+i)
                    .build();
            sido.updateDeleted(true);
            em.persist(sido);
        }
        em.flush();

        ///when
        List<Sido> sidos = sidoRepository.findAllNotDeleted();

        //then
        assertThat(sidos.size()).isEqualTo(0);
    }

    @Test
    public void delete_테스트() {
        //given
        String name = "서울시";
        Sido sido = Sido.builder()
                .name(name)
                .build();
        em.persistAndFlush(sido);

        ///when
        sidoRepository.delete(sido);
        em.flush();
        em.clear();

        //then
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(sido.getId());
        assertThat(findSido).isEmpty();
    }

}