package com.example.shelter.dong;

import com.example.shelter.common.dto.AreaSimpleDto;
import com.example.shelter.common.dto.ListDto;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.sigungu.SigunguService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}
