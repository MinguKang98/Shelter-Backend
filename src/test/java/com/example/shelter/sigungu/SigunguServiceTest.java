package com.example.shelter.sigungu;

import com.example.shelter.exception.notfound.SigunguNotFoundException;
import com.example.shelter.sido.Sido;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SigunguServiceTest {

    @Mock
    SigunguRepository sigunguRepository;

    @InjectMocks
    SigunguService sigunguService;

    @Test
    public void findAll_테스트(@Mock Sido sido) {
        //given
        List<Sigungu> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new Sigungu((long) i, "sigungu" + i, sido));
        }
        when(sigunguRepository.findAllNotDeleted(any())).thenReturn(result);

        ///when
        List<Sigungu> sigungus = sigunguService.findAll();

        //then
        assertThat(sigungus).isSortedAccordingTo(Comparator.comparing(Sigungu::getName));
        verify(sigunguRepository, times(1)).findAllNotDeleted(any());
    }

    @Test
    public void findAllBySido_테스트(@Mock Sido sido) {
        //given
        List<Sigungu> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new Sigungu((long) i, "sigungu" + i, sido));
        }
        result.sort(Comparator.comparing(Sigungu::getName));
        when(sigunguRepository.findAllBySidoNotDeleted(sido, Sort.by(Sort.Direction.ASC, "name"))).thenReturn(result);

        ///when
        List<Sigungu> sigungus = sigunguService.findAllBySido(sido);

        //then
        assertThat(sigungus).isSortedAccordingTo(Comparator.comparing(Sigungu::getName));
        verify(sigunguRepository, times(1))
                .findAllBySidoNotDeleted(sido, Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void findById_존재하는_시군구_테스트(@Mock Sido sido) {
        //given
        Long id = 1L;
        Sigungu sigungu = new Sigungu(id, "동대문구", sido);
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sigungu));

        ///when
        Sigungu findSigungu = sigunguService.findById(id);

        //then
        assertThat(findSigungu.getId()).isEqualTo(id);
        verify(sigunguRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_시군구_테스트() {
        //given
        Long id = 1L;
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sigunguService.findById(id)).isInstanceOf(SigunguNotFoundException.class);
        verify(sigunguRepository, times(1)).findByIdNotDeleted(id);
    }

    //save
    @Test
    public void save_테스트(@Mock Sido sido) {
        //given
        Long id = 1L;
        Sigungu sigungu = new Sigungu(id, "동대문구", sido);
        when(sigunguRepository.save(sigungu)).thenReturn(sigungu);

        ///when
        Long sigunguId = sigunguService.save(sigungu);

        //then
        assertThat(sigunguId).isEqualTo(id);
        verify(sigunguRepository, times(1)).save(sigungu);
    }

    //saveAll
    @Test
    public void saveAll_테스트(@Mock Sido sido) {
        //given
        List<Sigungu> sigungus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sigungus.add(new Sigungu((long) i, "sigungu" + i, sido));
        }
        when(sigunguRepository.saveAll(sigungus)).thenReturn(sigungus);

        ///when
        sigunguService.saveAll(sigungus);

        //then
        verify(sigunguRepository, times(1)).saveAll(sigungus);
    }

    @Test
    public void updateName_존재하는_시군구_테스트(@Mock Sido sido) {
        //given
        Long id = 1L;
        String newName = "동대문구";
        Sigungu sigungu = new Sigungu(id, "동대무구", sido);
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sigungu));

        ///when
        sigunguService.updateName(id, newName);

        //then
        assertThat(sigungu.getName()).isEqualTo(newName);
        verify(sigunguRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void updateName_삭제되거나_존재하지_않는_시군구_테스트() {
        //given
        Long id = 1L;
        String newName = "동대문구";
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sigunguService.updateName(id, newName)).isInstanceOf(SigunguNotFoundException.class);
    }

    @Test
    public void updateSido_존재하는_시군구_테스트(@Mock Sido sido) {
        //given
        Long id = 1L;
        Sido newSido = new Sido(1L, "서울특별시");
        Sigungu sigungu = new Sigungu(id, "동대문구", sido);
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sigungu));

        ///when
        sigunguService.updateSido(id, newSido);

        //then
        assertThat(sigungu.getSido().getId()).isEqualTo(newSido.getId());
        verify(sigunguRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void updateSido_삭제되거나_존재하지_않는_시군구_테스트(@Mock Sido newSido) {
        //given
        Long id = 1L;
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sigunguService.updateSido(id, newSido)).isInstanceOf(SigunguNotFoundException.class);
    }

    @Test
    public void delete_존재하는_시군구_테스트(@Mock Sido sido) {
        //given
        Long id = 1L;
        Sigungu sigungu = new Sigungu(id, "동대문구", sido);
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(sigungu));

        ///when
        sigunguService.delete(id);

        //then
        assertThat(sigungu.isDeleted()).isTrue();
        verify(sigunguRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void delete_삭제되거나_존재하지_않는_시군구_테스트() {
        //given
        Long id = 1L;
        when(sigunguRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> sigunguService.delete(id)).isInstanceOf(SigunguNotFoundException.class);
        verify(sigunguRepository, times(1)).findByIdNotDeleted(id);
    }

}