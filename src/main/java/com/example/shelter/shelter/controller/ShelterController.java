package com.example.shelter.shelter.controller;

import com.example.shelter.civildefenseshelter.service.CivilDefenseShelterService;
import com.example.shelter.common.dto.ListDto;
import com.example.shelter.earthquakeshelter.service.EarthquakeShelterService;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.ShelterVariable;
import com.example.shelter.shelter.dto.ShelterCountDto;
import com.example.shelter.shelter.dto.ShelterDistanceDto;
import com.example.shelter.shelter.service.ShelterService;
import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoService;
import com.example.shelter.tsunamishelter.service.TsunamiShelterService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;
    private final TsunamiShelterService tsunamiShelterService;
    private final EarthquakeShelterService earthquakeShelterService;
    private final CivilDefenseShelterService civilDefenseShelterService;
    private final SidoService sidoService;

    @GetMapping("/api/shelters/count")
    ResponseEntity<ShelterCountDto> getShelterCount(
            @RequestParam(value = "sido_id", required = false) Long sidoId) {

        int tsunamiCount;
        int earthquakeCount;
        int civilDefenseCount;

        if (sidoId == null) {
            tsunamiCount = tsunamiShelterService.countAll();
            earthquakeCount = earthquakeShelterService.countAll();
            civilDefenseCount = civilDefenseShelterService.countAll();
        } else {
            Sido sido = sidoService.findById(sidoId);
            tsunamiCount = tsunamiShelterService.countAllBySido(sido);
            earthquakeCount = earthquakeShelterService.countAllBySido(sido);
            civilDefenseCount = civilDefenseShelterService.countAllBySido(sido);
        }

        return ResponseEntity.ok(ShelterCountDto.of(tsunamiCount, earthquakeCount, civilDefenseCount));
    }

    @GetMapping("/api/shelters/current")
    ResponseEntity<ListDto<ShelterDistanceDto>> getSheltersByCurrent(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(value = "radius", required = false) @Positive Integer radius) {

        Integer radiusValue = Optional.ofNullable(radius).orElse(ShelterVariable.DEFAULT_RADIUS);
        List<Shelter> shelters = shelterService.findAllByCurrent(latitude, longitude, radiusValue);
        List<ShelterDistanceDto> shelterDistanceDtoList = shelters.stream()
                .map(s -> ShelterDistanceDto.of(s, latitude, longitude))
                .sorted(Comparator.comparing(ShelterDistanceDto::getDistance))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ListDto<>(shelterDistanceDtoList));
    }

}
