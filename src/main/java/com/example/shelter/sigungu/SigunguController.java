package com.example.shelter.sigungu;

import com.example.shelter.common.dto.AreaSimpleDto;
import com.example.shelter.common.dto.ListDto;
import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SigunguController {

    private final SidoService sidoService;
    private final SigunguService sigunguService;

    @GetMapping(value = "/api/sidos/{id}")
    public ResponseEntity<ListDto<AreaSimpleDto>> getSidos(@PathVariable("id") Long id) {
        Sido sido = sidoService.findById(id);
        List<Sigungu> sigungus = sigunguService.findAllBySido(sido);
        List<AreaSimpleDto> simpleDtoList = sigungus.stream()
                .map(AreaSimpleDto::ofSigungu)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ListDto<>(simpleDtoList));
    }

}
