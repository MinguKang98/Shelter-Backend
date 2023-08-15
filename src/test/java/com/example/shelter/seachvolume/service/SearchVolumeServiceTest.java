package com.example.shelter.seachvolume.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.seachvolume.dto.ShelterTypeVolumeDto;
import com.example.shelter.seachvolume.repository.SearchVolumeRepository;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchVolumeServiceTest {

    @Mock
    SearchVolumeRepository searchVolumeRepository;

    @InjectMocks
    SearchVolumeService searchVolumeService;


    @Test
    public void updateSearchVolume_생성_테스트(@Mock Dong dong) {
        //given
        SearchVolume searchVolume = SearchVolume.builder()
                .volume(1)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        when(searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(searchVolumeRepository.save(any(SearchVolume.class))).thenReturn(searchVolume);

        ///when
        searchVolumeService.updateSearchVolume(dong, ShelterType.TSUNAMI, LocalDate.now());

        //then
        verify(searchVolumeRepository, times(1))
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now());
        verify(searchVolumeRepository, times(1)).save(any(SearchVolume.class));
    }

    @Test
    public void updateSearchVolume_업데이트_테스트(@Mock Dong dong) {
        //given
        int initVolume = 1;
        SearchVolume searchVolume = SearchVolume.builder()
                .volume(initVolume)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        when(searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now()))
                .thenReturn(Optional.of(searchVolume));

        ///when
        searchVolumeService.updateSearchVolume(dong, ShelterType.TSUNAMI, LocalDate.now());

        //then
        assertThat(searchVolume.getVolume()).isEqualTo(initVolume + 1);
        verify(searchVolumeRepository, times(1))
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now());
    }

    @Test
    public void getTotalVolumeByDate_테스트() {
        //given
        LocalDate now = LocalDate.now();
        int result = 100;
        when(searchVolumeRepository.getTotalVolumeByDateNotDeleted(now)).thenReturn(result);

        ///when
        int totalVolume = searchVolumeService.getTotalVolumeByDate(now);

        //then
        assertThat(totalVolume).isEqualTo(result);
        verify(searchVolumeRepository, times(1))
                .getTotalVolumeByDateNotDeleted(now);
    }

    @Test
    public void getTotalVolumeByDateRange_테스트() {
        //given
        LocalDate to = LocalDate.now();
        LocalDate from = LocalDate.now().minusDays(7);
        int result = 100;
        when(searchVolumeRepository.getTotalVolumeByDateRangeNotDeleted(from, to)).thenReturn(result);

        ///when
        int totalVolume = searchVolumeService.getTotalVolumeByDateRange(from, to);

        //then
        assertThat(totalVolume).isEqualTo(result);
        verify(searchVolumeRepository, times(1))
                .getTotalVolumeByDateRangeNotDeleted(from, to);
    }

    @Test
    public void getSidoVolumeMap_테스트() {
        //given
        LocalDate now = LocalDate.now();
        List<RegionVolumeDto> regionVolumeDtoList = List.of(
                new RegionVolumeDto(ShelterType.TSUNAMI, "서울특별시", 10L),
                new RegionVolumeDto(ShelterType.TSUNAMI, "부산광역시", 20L),
                new RegionVolumeDto(ShelterType.EARTHQUAKE, "서울특별시", 30L),
                new RegionVolumeDto(ShelterType.EARTHQUAKE, "부산광역시", 40L),
                new RegionVolumeDto(ShelterType.CIVIL_DEFENCE, "서울특별시", 50L),
                new RegionVolumeDto(ShelterType.CIVIL_DEFENCE, "부산광역시", 60L)
        );
        when(searchVolumeRepository.countSidoByDateNotDeleted(now)).thenReturn(regionVolumeDtoList);

        ///when
        ShelterTypeVolumeDto shelterTypeVolumeDto = searchVolumeService.getSidoVolumeMap(now);

        //then
        assertThat(shelterTypeVolumeDto.getTsunami().get("서울특별시")).isEqualTo(10L);
        assertThat(shelterTypeVolumeDto.getTsunami().get("부산광역시")).isEqualTo(20L);
        assertThat(shelterTypeVolumeDto.getEarthquake().get("서울특별시")).isEqualTo(30L);
        assertThat(shelterTypeVolumeDto.getEarthquake().get("부산광역시")).isEqualTo(40L);
        assertThat(shelterTypeVolumeDto.getCivilDefense().get("서울특별시")).isEqualTo(50L);
        assertThat(shelterTypeVolumeDto.getCivilDefense().get("부산광역시")).isEqualTo(60L);
        verify(searchVolumeRepository, times(1)).countSidoByDateNotDeleted(now);
    }

    @Test
    public void getSigunguVolumeMap_테스트(@Mock Sido sido) {
        //given
        LocalDate now = LocalDate.now();
        List<RegionVolumeDto> regionVolumeDtoList = List.of(
                new RegionVolumeDto(ShelterType.TSUNAMI, "강동구", 10L),
                new RegionVolumeDto(ShelterType.TSUNAMI, "동대문구", 20L),
                new RegionVolumeDto(ShelterType.EARTHQUAKE, "강동구", 30L),
                new RegionVolumeDto(ShelterType.EARTHQUAKE, "동대문구", 40L),
                new RegionVolumeDto(ShelterType.CIVIL_DEFENCE, "강동구", 50L),
                new RegionVolumeDto(ShelterType.CIVIL_DEFENCE, "동대문구", 60L)
        );
        when(searchVolumeRepository.countSigunguBySidoAndDateNotDeleted(sido, now))
                .thenReturn(regionVolumeDtoList);

        ///when
        ShelterTypeVolumeDto shelterTypeVolumeDto = searchVolumeService.getSigunguVolumeMap(sido, now);

        //then
        assertThat(shelterTypeVolumeDto.getTsunami().get("강동구")).isEqualTo(10L);
        assertThat(shelterTypeVolumeDto.getTsunami().get("동대문구")).isEqualTo(20L);
        assertThat(shelterTypeVolumeDto.getEarthquake().get("강동구")).isEqualTo(30L);
        assertThat(shelterTypeVolumeDto.getEarthquake().get("동대문구")).isEqualTo(40L);
        assertThat(shelterTypeVolumeDto.getCivilDefense().get("강동구")).isEqualTo(50L);
        assertThat(shelterTypeVolumeDto.getCivilDefense().get("동대문구")).isEqualTo(60L);
        verify(searchVolumeRepository, times(1))
                .countSigunguBySidoAndDateNotDeleted(sido, now);
    }

    @Test
    public void getDateVolumeMap_테스트(@Mock Dong dong) {
        //given
        LocalDate to = LocalDate.now();
        LocalDate from = LocalDate.now().minusDays(7);
        List<SearchVolume> searchVolumeList = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            SearchVolume searchVolume1 = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(10)
                    .shelterType(ShelterType.TSUNAMI)
                    .dong(dong)
                    .build();
            searchVolumeList.add(searchVolume1);

            SearchVolume searchVolume2 = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(20)
                    .shelterType(ShelterType.EARTHQUAKE)
                    .dong(dong)
                    .build();
            searchVolumeList.add(searchVolume2);

            SearchVolume searchVolume3 = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(30)
                    .shelterType(ShelterType.CIVIL_DEFENCE)
                    .dong(dong)
                    .build();
            searchVolumeList.add(searchVolume3);
        }

        when(searchVolumeRepository
                .findAllByDongAndDateRangeNotDeleted(dong, from, to))
                .thenReturn(searchVolumeList);

        ///when
        ShelterTypeVolumeDto shelterTypeVolumeDto = searchVolumeService.getDateVolumeMap(dong, from, to);

        //then
        assertThat(shelterTypeVolumeDto.getTsunami().size()).isEqualTo(8);
        assertThat(shelterTypeVolumeDto.getEarthquake().size()).isEqualTo(8);
        assertThat(shelterTypeVolumeDto.getCivilDefense().size()).isEqualTo(8);
        verify(searchVolumeRepository, times(1))
                .findAllByDongAndDateRangeNotDeleted(dong, from, to);
    }

}