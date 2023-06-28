package com.example.shelter.sido;

import com.example.shelter.exception.duplicate.SidoNameDuplicateException;
import com.example.shelter.exception.notfound.SidoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SidoServiceTest {

    @Mock
    SidoRepository sidoRepository;

    @InjectMocks
    SidoService sidoService;

    @Test
    public void findAll_테스트() {
        //given
        List<Sido> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new Sido((long) i, "sido" + i));
        }
        result.sort(Comparator.comparing(Sido::getName));
        when(sidoRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(result);

        ///when
        List<Sido> sidos = sidoService.findAll();

        //then
        assertThat(sidos).isSortedAccordingTo(Comparator.comparing(Sido::getName));
        verify(sidoRepository, times(1)).findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void findById_존재하는_시도_테스트() {
        //given
        Long id = 1L;
        String name = "서울시";
        Sido sido = new Sido(id, name);
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sido));

        ///when
        Sido findSido = sidoService.findById(id);

        //then
        assertThat(findSido).isEqualTo(sido);
        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_시도_테스트() {
        //given
        Long id = 1L;
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sidoService.findById(id)).isInstanceOf(SidoNotFoundException.class);
        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findByName_존재하는_시도_테스트() {
        //given
        Long id = 1L;
        String name = "서울시";
        Sido sido = new Sido(id, name);
        when(sidoRepository.findByNameNotDeleted(name)).thenReturn(Optional.of(sido));

        ///when
        Sido findSido = sidoService.findByName(name);

        //then
        assertThat(findSido).isEqualTo(sido);
        verify(sidoRepository, times(1)).findByNameNotDeleted(name);
    }

    @Test
    public void findByName_삭제되거나_존재하지_않는_시도_테스트() {
        //given
        Long id = 1L;
        String name = "서울시";
        when(sidoRepository.findByNameNotDeleted(name)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sidoService.findByName(name)).isInstanceOf(SidoNotFoundException.class);
        verify(sidoRepository, times(1)).findByNameNotDeleted(name);
    }

    @Test
    public void save_테스트() {
        //given
        Long id = 1L;
        String name = "서울시";
        Sido sido = new Sido(id, name);
        when(sidoRepository.save(sido)).thenReturn(sido);

        ///when
        Long sidoId = sidoService.save(sido);

        //then
        assertThat(sidoId).isEqualTo(id);
        verify(sidoRepository, times(1)).save(sido);
    }

    @Test
    public void saveAll_테스트() {
        //given
        List<Sido> sidos = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            sidos.add(new Sido((long) i, "sido" + i));
        }
        when(sidoRepository.saveAll(sidos)).thenReturn(sidos);

        ///when
        sidoService.saveAll(sidos);

        //then
        verify(sidoRepository, times(1)).saveAll(sidos);
    }

    @Test
    public void updateName_새로운_이름_수정_테스트() {
        //given
        Long id = 1L;
        String newName = "서울특별시";
        Sido sido = new Sido(id, "서울시");
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sido));
        when(sidoRepository.findByNameNotDeleted(newName)).thenReturn(Optional.empty());

        ///when
        sidoService.updateName(id, newName);

        //then
        assertThat(sido.getName()).isEqualTo(newName);
        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
        verify(sidoRepository, times(1)).findByNameNotDeleted(newName);
    }

    @Test
    public void updateName_존재하는_이름_수정_테스트() {
        //given
        Long id = 1L;
        String newName = "경기도";
        Sido sido = new Sido(id, "서울시");
        Sido sido2 = new Sido(2L, newName);
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sido));
        when(sidoRepository.findByNameNotDeleted(newName)).thenReturn(Optional.of(sido2));

        ///when
        //then
        assertThatThrownBy(() -> sidoService.updateName(id, newName))
                .isInstanceOf(SidoNameDuplicateException.class);

        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
        verify(sidoRepository, times(1)).findByNameNotDeleted(newName);
    }

    @Test
    public void updateName_삭제되거나_존재하지_않는_시도_테스트() {
        //given
        Long id = 1L;
        String newName = "서울특별시";
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sidoService.updateName(id, newName))
                .isInstanceOf(SidoNotFoundException.class);
        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void delete_존재하는_시도_테스트() {
        //given
        Long id = 1L;
        Sido sido = new Sido(id, "서울특별시");
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sido));

        ///when
        sidoService.delete(id);

        //then
        assertThat(sido.isDeleted()).isEqualTo(true);
        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void delete_삭제되거나_존재하지_않는_시도_테스트() {
        //given
        Long id = 1L;
        when(sidoRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sidoService.delete(id))
                .isInstanceOf(SidoNotFoundException.class);
        verify(sidoRepository, times(1)).findByIdNotDeleted(id);
    }

}