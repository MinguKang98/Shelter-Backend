package com.example.shelter.civildefenseshelter;

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

@Validated
@RestController
@RequiredArgsConstructor
public class CivilDefenseShelterController {

    private final CivilDefenseShelterService civilDefenseShelterService;
    private final DongService dongService;

    @GetMapping("/api/shelters/civilDefense/{id}")
    public ResponseEntity<CivilDefenseShelterDto> getCivilDefenseShelter(@PathVariable("id") Long id) {
        CivilDefenseShelter civilDefenseShelter = civilDefenseShelterService.findById(id);
        return ResponseEntity.ok(CivilDefenseShelterDto.of(civilDefenseShelter));
    }

    @GetMapping("/api/shelters/civilDefense")
    public ResponseEntity<ShelterPageDto> getCivilDefenseSheltersByDong(@RequestParam("dong_id") Long dongId,
                                                                      @RequestParam("page") @Positive int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, ShelterVariable.PAGE_SIZE);
        Dong dong = dongService.findById(dongId);
        Page<CivilDefenseShelter> civilDefenseShelters = civilDefenseShelterService.findAllByDong(dong, pageRequest);
        return ResponseEntity.ok(ShelterPageDto.of(civilDefenseShelters));
    }

    @GetMapping("/api/shelters/civilDefense/current")
    public ResponseEntity<ShelterListDto> getCivilDefenseSheltersByCurrent(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(value = "radius", defaultValue = "2000") @Positive int radius) {

        List<CivilDefenseShelter> civilDefenseShelters = civilDefenseShelterService
                .findAllByCurrent(latitude, longitude, radius);
        return ResponseEntity.ok(ShelterListDto.of(civilDefenseShelters));
    }

}
