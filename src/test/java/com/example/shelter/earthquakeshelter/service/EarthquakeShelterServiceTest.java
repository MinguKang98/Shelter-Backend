package com.example.shelter.earthquakeshelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.repository.EarthquakeShelterRepository;
import com.example.shelter.exception.notfound.EarthquakeShelterNotFoundException;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.sido.Sido;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarthquakeShelterServiceTest {

    @Mock
    EarthquakeShelterRepository earthquakeShelterRepository;

    @InjectMocks
    EarthquakeShelterService earthquakeShelterService;

    @Test
    public void findById_존재하는_지진대피소_테스트(@Mock Dong dong) {
        //given
        Long id = 1L;
        EarthquakeShelter shelter = EarthquakeShelter.builder()
                .id(id)
                .name("대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .area(100)
                .build();
        when(earthquakeShelterRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(shelter));

        ///when
        EarthquakeShelter findShelter = earthquakeShelterService.findById(id);

        //then
        assertThat(findShelter.getId()).isEqualTo(id);
        verify(earthquakeShelterRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_지진대피소_테스트(@Mock Dong dong) {
        //given
        Long id = 1L;
        when(earthquakeShelterRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> earthquakeShelterService.findById(id))
                .isInstanceOf(EarthquakeShelterNotFoundException.class);
        verify(earthquakeShelterRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findAllByDong_테스트(@Mock Dong dong) {
        //given
        int totalSize = 30;
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "name"));
        List<EarthquakeShelter> content = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            EarthquakeShelter shelter = EarthquakeShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(100)
                    .build();
            content.add(shelter);
        }
        content.sort(Comparator.comparing(EarthquakeShelter::getName));
        PageImpl<EarthquakeShelter> shelters = new PageImpl<>(content, pageRequest, totalSize);
        when(earthquakeShelterRepository.findAllByDongNotDeleted(dong, pageRequest)).thenReturn(shelters);

        ///when
        Page<EarthquakeShelter> findShelters = earthquakeShelterService.findAllByDong(dong, pageRequest);

        //then
        assertThat(findShelters.getTotalElements()).isEqualTo(totalSize);
        assertThat(findShelters.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(findShelters.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(findShelters.getNumberOfElements()).isEqualTo(content.size());
        assertThat(findShelters.getContent()).isSortedAccordingTo(
                Comparator.comparing(EarthquakeShelter::getName));


        verify(earthquakeShelterRepository, times(1)).findAllByDongNotDeleted(dong, pageRequest);

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
        List<EarthquakeShelter> content = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            EarthquakeShelter shelter = EarthquakeShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .area(100)
                    .build();
            content.add(shelter);
        }

        when(earthquakeShelterRepository.findAllBySquareRangeNotDeleted(
                any(Double.class), any(Double.class), any(Double.class), any(Double.class)))
                .thenReturn(content);

        ///when
        List<EarthquakeShelter> shelters = earthquakeShelterService.findAllByCurrent(curLat, curLon, radius);

        //then
        assertThat(shelters.size()).isEqualTo(2);
        verify(earthquakeShelterRepository, times(1))
                .findAllBySquareRangeNotDeleted(
                        any(Double.class), any(Double.class), any(Double.class), any(Double.class));
    }

    @Test
    public void countAll_테스트() {
        //given
        int num = 10;
        when(earthquakeShelterRepository.countAllNotDeleted()).thenReturn(num);

        ///when
        int count = earthquakeShelterService.countAll();

        //then
        assertThat(count).isEqualTo(num);
        verify(earthquakeShelterRepository, times(1)).countAllNotDeleted();
    }

    @Test
    public void countAllBySido_테스트(@Mock Sido sido) {
        //given
        int num = 10;
        when(earthquakeShelterRepository.countAllBySidoNotDeleted(sido)).thenReturn(num);

        ///when
        int count = earthquakeShelterService.countAllBySido(sido);

        //then
        assertThat(count).isEqualTo(num);
        verify(earthquakeShelterRepository, times(1)).countAllBySidoNotDeleted(sido);
    }

}