package com.example.shelter.dong;

import com.example.shelter.exception.notfound.DongNotFoundException;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.util.NaverApiParser;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DongServiceTest {

    @Mock
    DongRepository dongRepository;

    @Mock
    NaverApiParser naverApiParser;

    @InjectMocks
    DongService dongService;

    @Test
    public void findAll_테스트(@Mock Sigungu sigungu) {
        //given
        int count = 10;
        List<Dong> results = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            results.add(new Dong((long) i, "dong" + i, sigungu));
        }
        results.sort(Comparator.comparing(Dong::getName));
        when(dongRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(results);

        ///when
        List<Dong> dongs = dongService.findAll();

        //then
        assertThat(dongs.size()).isEqualTo(count);
        assertThat(dongs).isSortedAccordingTo(Comparator.comparing(Dong::getName));
        verify(dongRepository, times(1))
                .findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void findAllBySigungu_테스트(@Mock Sigungu sigungu) {
        //given
        int count = 10;
        List<Dong> results = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            results.add(new Dong((long) i, "dong" + i, sigungu));
        }
        results.sort(Comparator.comparing(Dong::getName));
        when(dongRepository.findAllBySigunguNotDeleted(sigungu, Sort.by(Sort.Direction.ASC, "name")))
                .thenReturn(results);

        ///when
        List<Dong> dongs = dongService.findAllBySigungu(sigungu);

        //then
        assertThat(dongs.size()).isEqualTo(count);
        assertThat(dongs).isSortedAccordingTo(Comparator.comparing(Dong::getName));
        verify(dongRepository, times(1))
                .findAllBySigunguNotDeleted(sigungu, Sort.by(Sort.Direction.ASC, "name"));
    }


    @Test
    public void findById_존재하는_동_테스트(@Mock Sigungu sigungu) {
        //given
        Long id = 1L;
        Dong dong = new Dong(id, "전농동", sigungu);
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(dong));

        ///when
        Dong findDong = dongService.findById(id);

        //then
        assertThat(findDong.getId()).isEqualTo(dong.getId());
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_동_테스트() {
        //given
        Long id = 1L;
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> dongService.findById(id)).isInstanceOf(DongNotFoundException.class);
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void save_테스트(@Mock Sigungu sigungu) {
        //given
        Long id = 1L;
        Dong dong = new Dong(id, "전농동", sigungu);
        when(dongRepository.save(dong)).thenReturn(dong);

        ///when
        Long dongId = dongService.save(dong);

        //then
        assertThat(dongId).isEqualTo(id);
        verify(dongRepository, times(1)).save(dong);
    }

    @Test
    public void saveAll_테스트(@Mock Sigungu sigungu) {
        //given
        int count = 10;
        List<Dong> dongs = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            dongs.add(new Dong((long) i, "dong" + i, sigungu));
        }

        ///when
        dongService.saveAll(dongs);

        //then
        verify(dongRepository, times(1)).saveAll(dongs);
    }

    @Test
    public void updateName_존재하는_동_테스트(@Mock Sigungu sigungu) {
        //given
        Long id = 1L;
        String newName = "전농동";
        Dong dong = new Dong(id, "전노동", sigungu);
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(dong));

        ///when
        dongService.updateName(id, newName);

        //then
        assertThat(dong.getName()).isEqualTo(newName);
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void updateName_삭제되거나_존재하지_않는_동_테스트() {
        //given
        Long id = 1L;
        String newName = "전농동";
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> dongService.updateName(id, newName)).isInstanceOf(DongNotFoundException.class);
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void updateSigungu_존재하는_동_테스트(@Mock Sigungu sigungu, @Mock Sido sido) {
        //given
        Long id = 1L;
        Dong dong = new Dong(id, "전농동", sigungu);
        Sigungu newSigungu = new Sigungu(2L, "동대문구", sido);
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(dong));

        ///when
        dongService.updateSigungu(id, newSigungu);

        //then
        assertThat(dong.getSigungu().getId()).isEqualTo(newSigungu.getId());
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }


    @Test
    public void updateSigungu_삭제되거나_존재하지_않는_동_테스트(@Mock Sigungu newSigungu) {
        //given
        Long id = 1L;
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> dongService.updateSigungu(id, newSigungu)).isInstanceOf(DongNotFoundException.class);
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void delete_존재하는_동_테스트(@Mock Sigungu sigungu) {
        //given
        Long id = 1L;
        Dong dong = new Dong(id, "전농동", sigungu);
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(dong));

        ///when
        dongService.delete(id);

        //then
        assertThat(dong.isDeleted()).isTrue();
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void delete_삭제되거나_존재하지_않는_동_테스트() {
        //given
        Long id = 1L;
        when(dongRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> dongService.delete(id)).isInstanceOf(DongNotFoundException.class);
        verify(dongRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findByCurrent_테스트(@Mock Sigungu sigungu) {
        //given
        Long id = 1L;
        Dong dong = new Dong(id, "전농동", sigungu);
        double lat = 37.577400;
        double lon = 127.065355;
        Address address = new Address("서울특별시", "동대문구", "전농동", "");
        when(naverApiParser.getAddressByCurrent(lat, lon)).thenReturn(address);
        when(dongRepository.findByAddressNames(address.getSidoName(), address.getSigunguName(), address.getDongName()))
                .thenReturn(Optional.of(dong));

        ///when
        Dong currentDong = dongService.findByCurrent(lat, lon);

        //then
        assertThat(currentDong.getId()).isEqualTo(id);
    }

    @Test
    public void findByCurrent_예외_테스트(@Mock Sigungu sigungu) {
        //given
        Long id = 1L;
        Dong dong = new Dong(id, "전농동", sigungu);
        double lat = 37.577400;
        double lon = 127.065355;
        Address address = new Address("서울특별시", "동대문구", "전농동", "");
        when(naverApiParser.getAddressByCurrent(lat, lon)).thenReturn(address);
        when(dongRepository.findByAddressNames(address.getSidoName(), address.getSigunguName(), address.getDongName()))
                .thenReturn(Optional.empty());

        ///when
        Exception exception = catchException(() -> dongService.findByCurrent(lat, lon));

        //then
        assertThat(exception).isInstanceOf(DongNotFoundException.class);
        DongNotFoundException dongNotFoundException = (DongNotFoundException) exception;
        assertThat(dongNotFoundException.getErrors().get("name")).isEqualTo(address.getFullDongName());
    }

}