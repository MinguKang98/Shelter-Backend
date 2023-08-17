package com.example.shelter.earthquakeshelter.controller;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.dto.EarthquakeShelterDto;
import com.example.shelter.earthquakeshelter.service.EarthquakeShelterService;
import com.example.shelter.shelter.ShelterVariable;
import com.example.shelter.shelter.dto.ShelterListDto;
import com.example.shelter.shelter.dto.ShelterPageDto;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
public class EarthquakeShelterController {

    private final EarthquakeShelterService earthquakeShelterService;
    private final DongService dongService;

    @GetMapping("/api/shelters/earthquake/{id}")
    public ResponseEntity<EarthquakeShelterDto> getEarthquakeShelter(@PathVariable("id") Long id) {
        EarthquakeShelter earthquakeShelter = earthquakeShelterService.findById(id);
        return ResponseEntity.ok(EarthquakeShelterDto.of(earthquakeShelter));
    }

    @GetMapping("/api/shelters/earthquake")
    public ResponseEntity<ShelterPageDto> getEarthquakeSheltersByDong(@RequestParam("dong_id") Long dongId,
                                                                      @RequestParam("page") @Positive int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, ShelterVariable.PAGE_SIZE);
        Dong dong = dongService.findById(dongId);
        Page<EarthquakeShelter> earthquakeShelters = earthquakeShelterService.findAllByDong(dong, pageRequest);
        return ResponseEntity.ok(ShelterPageDto.of(earthquakeShelters));
    }

    @GetMapping("/api/shelters/earthquake/current")
    public ResponseEntity<ShelterListDto> getEarthquakeSheltersByCurrent(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(value = "radius", required = false) @Positive Integer radius) {

        Integer radiusValue = Optional.ofNullable(radius).orElse(ShelterVariable.DEFAULT_RADIUS);
        List<EarthquakeShelter> earthquakeShelters = earthquakeShelterService
                .findAllByCurrent(latitude, longitude, radiusValue);
        return ResponseEntity.ok(ShelterListDto.of(earthquakeShelters));
    }

}
