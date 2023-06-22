package com.example.shelter.sido;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    EntityManager em;

    @Test
    public void save_테스트() {
        //given
        String name = "서울시";

        ///when
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());
        em.flush();
        em.clear();

        //then
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(saveSido.getId());
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getId()).isEqualTo(saveSido.getId());
    }

    @Test
    public void findByIdNotDeleted_존재하는_시도_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());
        em.flush();
        em.clear();

        ///when
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(saveSido.getId());

        //then
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getId()).isEqualTo(saveSido.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_시도_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());
        saveSido.updateDeleted(true);
        em.flush();
        em.clear();

        ///when
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(saveSido.getId());

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
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());

        ///when
        Optional<Sido> findSido = sidoRepository.findByNameNotDeleted(name);

        //then
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get()).isEqualTo(saveSido);
    }

    @Test
    public void findByNameNotDeleted_삭제된_시도_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());
        saveSido.updateDeleted(true);

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
            sidoRepository.save(Sido.builder().name("sido" + i).build());
        }

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
            sidoRepository.save(Sido.builder().name("sido" + i).build());
        }

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
            Sido sido = Sido.builder().name("sido" + i).build();
            sido.updateDeleted(true);
            sidoRepository.save(sido);
        }

        ///when
        List<Sido> sidos = sidoRepository.findAllNotDeleted();

        //then
        assertThat(sidos.size()).isEqualTo(0);
    }

    @Test
    public void delete_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());

        ///when
        sidoRepository.delete(saveSido);
        em.flush();
        em.clear();

        //then
        Optional<Sido> findSido = sidoRepository.findByIdNotDeleted(saveSido.getId());
        assertThat(findSido).isEmpty();
    }

}