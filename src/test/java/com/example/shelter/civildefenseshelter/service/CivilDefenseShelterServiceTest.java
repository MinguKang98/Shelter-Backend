package com.example.shelter.civildefenseshelter.service;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.civildefenseshelter.repository.CivilDefenseShelterRepository;
import com.example.shelter.dong.Dong;
import com.example.shelter.exception.notfound.CivilDefenseShelterNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CivilDefenseShelterServiceTest {

    @Mock
    CivilDefenseShelterRepository civilDefenseShelterRepository;

    @InjectMocks
    CivilDefenseShelterService civilDefenseShelterService;

    @Test
    public void findById_존재하는_민방위대피소_테스트(@Mock Dong dong) {
        //given
        Long id = 1L;
        CivilDefenseShelter shelter = CivilDefenseShelter.builder()
                .id(id)
                .name("대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(dong)
                .roadAddress("도로명주소")
                .area(100)
                .type("공공시설")
                .build();
        when(civilDefenseShelterRepository.findByIdNotDeleted(id)).thenReturn(Optional.of(shelter));

        ///when
        CivilDefenseShelter findShelter = civilDefenseShelterService.findById(id);

        //then
        assertThat(findShelter.getId()).isEqualTo(id);
        verify(civilDefenseShelterRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findById_삭제되거나_존재하지_않는_민방위대피소_테스트(@Mock Dong dong) {
        //given
        Long id = 1L;
        when(civilDefenseShelterRepository.findByIdNotDeleted(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> civilDefenseShelterService.findById(id))
                .isInstanceOf(CivilDefenseShelterNotFoundException.class);
        verify(civilDefenseShelterRepository, times(1)).findByIdNotDeleted(id);
    }

    @Test
    public void findAllByDong_테스트(@Mock Dong dong) {
        //given
        int totalSize = 30;
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "name"));
        List<CivilDefenseShelter> content = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            CivilDefenseShelter shelter = CivilDefenseShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .roadAddress("도로명주소")
                    .area(100)
                    .type("공공시설")
                    .build();
            content.add(shelter);
        }
        content.sort(Comparator.comparing(CivilDefenseShelter::getName));
        PageImpl<CivilDefenseShelter> shelters = new PageImpl<>(content, pageRequest, totalSize);
        when(civilDefenseShelterRepository.findAllByDongNotDeleted(dong, pageRequest)).thenReturn(shelters);

        ///when
        Page<CivilDefenseShelter> findShelters = civilDefenseShelterService.findAllByDong(dong, pageRequest);

        //then
        assertThat(findShelters.getTotalElements()).isEqualTo(totalSize);
        assertThat(findShelters.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(findShelters.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(findShelters.getNumberOfElements()).isEqualTo(content.size());
        assertThat(findShelters.getContent()).isSortedAccordingTo(
                Comparator.comparing(CivilDefenseShelter::getName));


        verify(civilDefenseShelterRepository, times(1)).findAllByDongNotDeleted(dong, pageRequest);
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
        List<CivilDefenseShelter> content = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            CivilDefenseShelter shelter = CivilDefenseShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(gpsList.get(i)[0])
                    .longitude(gpsList.get(i)[1])
                    .dong(dong)
                    .roadAddress("도로명주소")
                    .area(100)
                    .type("공공시설")
                    .build();
            content.add(shelter);
        }

        when(civilDefenseShelterRepository.findAllBySquareRangeNotDeleted(
                any(Double.class), any(Double.class), any(Double.class), any(Double.class)))
                .thenReturn(content);

        ///when
        List<CivilDefenseShelter> shelters = civilDefenseShelterService.findAllByCurrent(curLat, curLon, radius);

        //then
        assertThat(shelters.size()).isEqualTo(2);
        verify(civilDefenseShelterRepository, times(1))
                .findAllBySquareRangeNotDeleted(
                        any(Double.class), any(Double.class), any(Double.class), any(Double.class));
    }

    @Test
    public void countAll_테스트() {
        //given
        int num = 10;
        when(civilDefenseShelterRepository.countAllNotDeleted()).thenReturn(num);

        ///when
        int count = civilDefenseShelterService.countAll();

        //then
        assertThat(count).isEqualTo(num);
        verify(civilDefenseShelterRepository, times(1)).countAllNotDeleted();
    }

    @Test
    public void countAllBySido_테스트(@Mock Sido sido) {
        //given
        int num = 10;
        when(civilDefenseShelterRepository.countAllBySidoNotDeleted(sido)).thenReturn(num);

        ///when
        int count = civilDefenseShelterService.countAllBySido(sido);

        //then
        assertThat(count).isEqualTo(num);
        verify(civilDefenseShelterRepository, times(1)).countAllBySidoNotDeleted(sido);
    }

}