package com.example.shelter.sido;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        Optional<Sido> findSido = sidoRepository.findById(saveSido.getId());
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getId()).isEqualTo(saveSido.getId());
    }

    @Test
    public void findById_존재함_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());
        em.flush();
        em.clear();

        ///when
        Optional<Sido> findSido = sidoRepository.findById(saveSido.getId());

        //then
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getId()).isEqualTo(saveSido.getId());
    }

    @Test
    public void findById_존재안함_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<Sido> findSido = sidoRepository.findById(id);

        //then
        assertThat(findSido).isEmpty();
    }

    @Test
    public void findByName_존재함_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());

        ///when
        Optional<Sido> findSido = sidoRepository.findByName(name);

        //then
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get()).isEqualTo(saveSido);
    }

    @Test
    public void findByName_존재안함_테스트() {
        //given
        String name = "서울시";

        ///when
        Optional<Sido> findSido = sidoRepository.findByName(name);

        //then
        assertThat(findSido).isEmpty();
    }

    @Test
    public void findAll_테스트() {
        //given
        int count = 10;
        for (int i = 1; i < count + 1; i++) {
            sidoRepository.save(Sido.builder().name("sido" + i).build());
        }

        ///when
        List<Sido> sidos = sidoRepository.findAll();

        //then
        assertThat(sidos.size()).isEqualTo(count);
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
        Optional<Sido> findSido = sidoRepository.findById(saveSido.getId());
        assertThat(findSido).isEmpty();
    }

    @Test
    public void update_테스트() {
        //given
        String name = "서울시";
        Sido saveSido = sidoRepository.save(Sido.builder().name(name).build());

        ///when
        String newName = "서울특별시";
        saveSido.updateName(newName);
        em.flush();
        em.clear();

        //then
        Optional<Sido> findSido = sidoRepository.findById(saveSido.getId());
        assertThat(findSido).isNotEmpty();
        assertThat(findSido.get().getName()).isEqualTo(newName);
    }

}