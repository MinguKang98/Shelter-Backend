package com.example.shelter.dong;

import com.example.shelter.common.dto.AreaSimpleDto;
import com.example.shelter.common.dto.ListDto;
import com.example.shelter.dong.dto.DongDetailDto;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.sigungu.SigunguService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DongController {

    private final SigunguService sigunguService;
    private final DongService dongService;

    @GetMapping(value = "/api/sigungus/{id}")
    public ResponseEntity<ListDto<AreaSimpleDto>> getSidos(@PathVariable("id") Long id) {
        Sigungu sigungu = sigunguService.findById(id);
        List<Dong> dongs = dongService.findAllBySigungu(sigungu);
        List<AreaSimpleDto> simpleDtoList = dongs.stream()
                .map(AreaSimpleDto::ofDong)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ListDto<>(simpleDtoList));
    }

    @GetMapping(value = "/api/dongs/current")
    public ResponseEntity<DongDetailDto> getDongByCurrent(@RequestParam("lat") double latitude,
                                              @RequestParam("lon") double longitude) {

        Dong dong = dongService.findByCurrent(latitude, longitude);
        Sigungu sigungu = dong.getSigungu();
        Sido sido = dong.getSigungu().getSido();
        return ResponseEntity.ok(new DongDetailDto(sido, sigungu, dong));
    }

}
