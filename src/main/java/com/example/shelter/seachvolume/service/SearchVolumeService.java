package com.example.shelter.seachvolume.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.seachvolume.dto.ShelterTypeVolumeDto;
import com.example.shelter.seachvolume.repository.SearchVolumeRepository;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchVolumeService {

    private final SearchVolumeRepository searchVolumeRepository;

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
        List<RegionVolumeDto> regionVolumeDtoList = searchVolumeRepository.countSidoByDateNotDeleted(date);
        return ShelterTypeVolumeDto.ofRegionVolume(regionVolumeDtoList);
    }

    public ShelterTypeVolumeDto getSigunguVolumeMap(Sido sido, LocalDate date) {
        List<RegionVolumeDto> regionVolumeDtoList = searchVolumeRepository
                .countSigunguBySidoAndDateNotDeleted(sido, date);
        return ShelterTypeVolumeDto.ofRegionVolume(regionVolumeDtoList);
    }

    public ShelterTypeVolumeDto getDateVolumeMap(Dong dong, LocalDate from, LocalDate to) {
        List<SearchVolume> searchVolumeList = searchVolumeRepository
                .findAllByDongAndDateRangeNotDeleted(dong, from, to);
        return ShelterTypeVolumeDto.ofSearchVolume(searchVolumeList);
    }

}
