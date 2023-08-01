package com.example.shelter.seachvolume.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.seachvolume.dto.ShelterVolumeDto;
import com.example.shelter.seachvolume.repository.SearchVolumeRepository;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, ShelterVolumeDto> getSidoVolumeMap(LocalDate date) {
        return changeToRegionVolumeMap(searchVolumeRepository.countSidoByDateNotDeleted(date));
    }

    public Map<String, ShelterVolumeDto> getSigunguVolumeMap(Sido sido, LocalDate date) {
        return changeToRegionVolumeMap(searchVolumeRepository.countSigunguBySidoAndDateNotDeleted(sido, date));
    }

//    public Map<LocalDate, ShelterVolumeDto> getDateVolumeMap(Dong dong, ShelterType type,
//                                                             LocalDate from, LocalDate to) {
//        return changeToDateVolumeMap(searchVolumeRepository
//                .findAllByDongAndTypeAndDateRangeNotDeleted(dong, type, from, to));
//    }



    private Map<String, ShelterVolumeDto> changeToRegionVolumeMap(List<RegionVolumeDto> dtoList) {
        Map<String, ShelterVolumeDto> map = new HashMap<>();
        for (RegionVolumeDto volumeDto : dtoList) {
            if (!map.containsKey(volumeDto.getName())) {
                map.put(volumeDto.getName(), new ShelterVolumeDto());
            }
            switch (volumeDto.getShelterType()) {
                case TSUNAMI -> map.get(volumeDto.getName()).setTsunami(volumeDto.getCount());
                case EARTHQUAKE -> map.get(volumeDto.getName()).setEarthquake(volumeDto.getCount());
                case CIVIL_DEFENCE -> map.get(volumeDto.getName()).setCivilDefense(volumeDto.getCount());
            }
        }
        return map;
    }

    private Map<LocalDate, ShelterVolumeDto> changeToDateVolumeMap(List<SearchVolume> list) {
        Map<LocalDate, ShelterVolumeDto> map = new HashMap<>();
        for (SearchVolume volume : list) {
            if (!map.containsKey(volume.getCreatedDate())) {
                map.put(volume.getCreatedDate(), new ShelterVolumeDto());
            }
            switch (volume.getShelterType()) {
                case TSUNAMI -> map.get(volume.getCreatedDate()).setTsunami(volume.getVolume());
                case EARTHQUAKE -> map.get(volume.getCreatedDate()).setEarthquake(volume.getVolume());
                case CIVIL_DEFENCE -> map.get(volume.getCreatedDate()).setCivilDefense(volume.getVolume());
            }
        }
        return map;
    }

}
