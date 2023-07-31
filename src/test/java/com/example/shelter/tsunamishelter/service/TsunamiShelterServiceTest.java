package com.example.shelter.tsunamishelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.exception.notfound.TsunamiShelterNotFoundException;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.sido.Sido;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.repository.TsunamiShelterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TsunamiShelterServiceTest {

    @Mock
    TsunamiShelterRepository tsunamiShelterRepository;

    @InjectMocks
    TsunamiShelterService tsunamiShelterService;

    @Test
    public void findById_존재하는_민방위대피소_테스트(@Mock Dong dong) {
        //given
        Long id = 1L;
        TsunamiShelter shelter = TsunamiShelter.builder()
                .id(id)
                .name("대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .capacity(100)
                .height(10)
                .length(10)
                .type("학교")
                .build();
        when(tsunamiShelterRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(shelter));

        ///when
        TsunamiShelter findShelter = tsunamiShelterService.findById(id);

        //then
        assertThat(findShelter.getId()).isEqualTo(id);
        verify(tsunamiShelterRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_민방위대피소_테스트(@Mock Dong dong) {
        //given
        Long id = 1L;
        when(tsunamiShelterRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> tsunamiShelterService.findById(id))
                .isInstanceOf(TsunamiShelterNotFoundException.class);
        verify(tsunamiShelterRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findAllByDong_테스트(@Mock Dong dong) {
        //given
        int totalSize = 30;
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "name"));
        List<TsunamiShelter> content = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            TsunamiShelter shelter = TsunamiShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            content.add(shelter);
        }
        content.sort(Comparator.comparing(TsunamiShelter::getName));
        PageImpl<TsunamiShelter> shelters = new PageImpl<>(content, pageRequest, totalSize);
        when(tsunamiShelterRepository.findAllByDongNotDeleted(dong, pageRequest)).thenReturn(shelters);

        ///when
        Page<TsunamiShelter> findShelters = tsunamiShelterService.findAllByDong(dong, pageRequest);

        //then
        assertThat(findShelters.getTotalElements()).isEqualTo(totalSize);
        assertThat(findShelters.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(findShelters.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(findShelters.getNumberOfElements()).isEqualTo(content.size());
        assertThat(findShelters.getContent()).isSortedAccordingTo(
                Comparator.comparing(TsunamiShelter::getName));


        verify(tsunamiShelterRepository, times(1)).findAllByDongNotDeleted(dong, pageRequest);
    }

    @Test
    public void findAllByCurrent_테스트(@Mock Dong dong) {
        //given
        double curLat = 37.577400;
        double curLon = 127.065355;
        double radius = 200;

        List<double[]> gpsList = List.of(
                new double[]{37.577401, 127.065352},
                new double[]{37.578055, 127.065961},
                new double[]{37.575602, 127.063086},
                new double[]{37.575602, 127.067623},
                new double[]{37.579198, 127.063086},
                new double[]{37.579198, 127.067623}
        );
        List<TsunamiShelter> content = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TsunamiShelter shelter = TsunamiShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            content.add(shelter);
        }

        when(tsunamiShelterRepository.findAllBySquareRangeNotDeleted(
                any(Double.class), any(Double.class), any(Double.class), any(Double.class)))
                .thenReturn(content);

        ///when
        List<TsunamiShelter> shelters = tsunamiShelterService.findAllByCurrent(curLat, curLon, radius);

        //then
        assertThat(shelters.size()).isEqualTo(2);
        verify(tsunamiShelterRepository, times(1))
                .findAllBySquareRangeNotDeleted(
                        any(Double.class), any(Double.class), any(Double.class), any(Double.class));
    }

    @Test
    public void countAll_테스트() {
        //given
        int num = 10;
        when(tsunamiShelterRepository.countAllNotDeleted()).thenReturn(num);

        ///when
        int count = tsunamiShelterService.countAll();

        //then
        assertThat(count).isEqualTo(num);
        verify(tsunamiShelterRepository, times(1)).countAllNotDeleted();
    }

    @Test
    public void countAllBySido_테스트(@Mock Sido sido) {
        //given
        int num = 10;
        when(tsunamiShelterRepository.countAllBySidoNotDeleted(sido)).thenReturn(num);

        ///when
        int count = tsunamiShelterService.countAllBySido(sido);

        //then
        assertThat(count).isEqualTo(num);
        verify(tsunamiShelterRepository, times(1)).countAllBySidoNotDeleted(sido);
    }

}