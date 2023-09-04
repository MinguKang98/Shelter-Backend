package com.example.shelter.civildefenseshelter.controller;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.civildefenseshelter.dto.CivilDefenseShelterDto;
import com.example.shelter.civildefenseshelter.service.CivilDefenseShelterService;
import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
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
public class CivilDefenseShelterController {

    private final CivilDefenseShelterService civilDefenseShelterService;
    private final DongService dongService;

    @GetMapping("/api/shelters/civil-defense/{id}")
    public ResponseEntity<CivilDefenseShelterDto> getCivilDefenseShelter(@PathVariable("id") Long id) {
        CivilDefenseShelter civilDefenseShelter = civilDefenseShelterService.findById(id);
        return ResponseEntity.ok(CivilDefenseShelterDto.of(civilDefenseShelter));
    }

    @GetMapping("/api/shelters/civil-defense")
    public ResponseEntity<ShelterPageDto> getCivilDefenseSheltersByDong(@RequestParam("dong_id") Long dongId,
                                                                      @RequestParam("page") @Positive int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, ShelterVariable.PAGE_SIZE);
        Dong dong = dongService.findById(dongId);
        Page<CivilDefenseShelter> civilDefenseShelters = civilDefenseShelterService.findAllByDong(dong, pageRequest);
        return ResponseEntity.ok(ShelterPageDto.of(civilDefenseShelters));
    }

    @GetMapping("/api/shelters/civil-defense/current")
    public ResponseEntity<ShelterListDto> getCivilDefenseSheltersByCurrent(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(value = "radius", required = false) @Positive Integer radius) {

        Integer radiusValue = Optional.ofNullable(radius).orElse(ShelterVariable.DEFAULT_RADIUS);
        List<CivilDefenseShelter> civilDefenseShelters = civilDefenseShelterService
                .findAllByCurrent(latitude, longitude, radiusValue);
        return ResponseEntity.ok(ShelterListDto.of(civilDefenseShelters));
    }

}
