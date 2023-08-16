package com.example.shelter.seachvolume.controller;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
import com.example.shelter.seachvolume.dto.IncreaseInfoDto;
import com.example.shelter.seachvolume.dto.ShelterTypeVolumeDto;
import com.example.shelter.seachvolume.dto.TotalDto;
import com.example.shelter.seachvolume.service.SearchVolumeService;
import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Validated
@RestController
@RequiredArgsConstructor
public class SearchVolumeController {

    private final SearchVolumeService searchVolumeService;
    private final SidoService sidoService;
    private final DongService dongService;


    @PutMapping("/api/search-volumes")
    public ResponseEntity<Void> increaseSearchVolumes(@Valid @RequestBody IncreaseInfoDto increaseInfoDto) {
        LocalDate now = LocalDate.now();
        Dong dong = dongService.findById(increaseInfoDto.getDongId());

        searchVolumeService.updateSearchVolume(dong, increaseInfoDto.getType(), now);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/search-volumes/total")
    public ResponseEntity<TotalDto> getTotalSearchVolume() {
        LocalDate now = LocalDate.now();
        int totalVolume = searchVolumeService.getTotalVolumeByDate(now);
        return ResponseEntity.ok(TotalDto.of(totalVolume));
    }

    @GetMapping("/api/search-volumes/week-total")
    public ResponseEntity<TotalDto> getWeekTotalSearchVolume() {
        LocalDate now = LocalDate.now();
        int totalVolume = searchVolumeService.getTotalVolumeByDateRange(now.minusDays(7), now);
        return ResponseEntity.ok(TotalDto.of(totalVolume));
    }

    @GetMapping("/api/search-volumes/sidos")
    public ResponseEntity<ShelterTypeVolumeDto> getSidoVolumes() {
        LocalDate now = LocalDate.now();
        ShelterTypeVolumeDto sidoVolumes = searchVolumeService.getSidoVolumeMap(now);
        return ResponseEntity.ok(sidoVolumes);
    }

    @GetMapping("/api/search-volumes/sidos/{id}")
    public ResponseEntity<ShelterTypeVolumeDto> getSigunguVolumes(@PathVariable("id") Long id) {
        LocalDate now = LocalDate.now();
        Sido sido = sidoService.findById(id);
        ShelterTypeVolumeDto sigunguVolumes = searchVolumeService.getSigunguVolumeMap(sido, now);
        return ResponseEntity.ok(sigunguVolumes);
    }

    @GetMapping("/api/search-volumes/week/dongs/{id}")
    public ResponseEntity<ShelterTypeVolumeDto> getDateVolumes(@PathVariable("id") Long id) {
        LocalDate now = LocalDate.now();
        Dong dong = dongService.findById(id);
        ShelterTypeVolumeDto dateVolumes = searchVolumeService.getDateVolumeMap(dong, now.minusDays(7), now);
        return ResponseEntity.ok(dateVolumes);
    }

}
