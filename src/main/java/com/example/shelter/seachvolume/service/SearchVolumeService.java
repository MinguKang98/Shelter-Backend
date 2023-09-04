package com.example.shelter.seachvolume.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.seachvolume.dto.ShelterTypeVolumeDto;
import com.example.shelter.seachvolume.repository.SearchVolumeRepository;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoRepository;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.sigungu.SigunguRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchVolumeService {

    private final SearchVolumeRepository searchVolumeRepository;
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;

    @Transactional
    public void updateSearchVolume(Dong dong, ShelterType type, LocalDate date) {
        searchVolumeRepository.findByDongAndTypeAndDateNotDeleted(dong, type, date)
                .ifPresentOrElse(SearchVolume::updateVolume,
                        () -> searchVolumeRepository.save(SearchVolume.builder()
                                .volume(1)
                                .dong(dong)
                                .shelterType(type)
                                .build()));
    }

    public int getTotalVolumeByDate(LocalDate date) {
        return searchVolumeRepository.getTotalVolumeByDateNotDeleted(date);
    }

    public int getTotalVolumeByDateRange(LocalDate from, LocalDate to) {
        return searchVolumeRepository.getTotalVolumeByDateRangeNotDeleted(from, to);
    }

    public ShelterTypeVolumeDto getSidoVolumeMap(LocalDate date) {
        List<String> sidoList = sidoRepository.findAllNotDeleted().stream()
                .map(Sido::getName)
                .collect(Collectors.toList());

        List<RegionVolumeDto> regionVolumeDtoList = searchVolumeRepository.countSidoByDateNotDeleted(date);
        return ShelterTypeVolumeDto.ofRegionVolume(regionVolumeDtoList, sidoList);
    }

    public ShelterTypeVolumeDto getSigunguVolumeMap(Sido sido, LocalDate date) {
        List<String> sigunguList = sigunguRepository
                .findAllBySidoNotDeleted(sido, Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(Sigungu::getName)
                .collect(Collectors.toList());

        List<RegionVolumeDto> regionVolumeDtoList = searchVolumeRepository
                .countSigunguBySidoAndDateNotDeleted(sido, date);
        return ShelterTypeVolumeDto.ofRegionVolume(regionVolumeDtoList, sigunguList);
    }

    public ShelterTypeVolumeDto getDateVolumeMap(Dong dong, LocalDate from, LocalDate to) {
        List<SearchVolume> searchVolumeList = searchVolumeRepository
                .findAllByDongAndDateRangeNotDeleted(dong, from, to);

        List<String> dateList = from.datesUntil(to.plusDays(1))
                .map(d -> d.format(ShelterTypeVolumeDto.formatter))
                .collect(Collectors.toList());
        return ShelterTypeVolumeDto.ofSearchVolume(searchVolumeList, dateList);
    }

}
