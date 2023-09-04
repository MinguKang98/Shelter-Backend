package com.example.shelter.sido;

import com.example.shelter.common.dto.AreaSimpleDto;
import com.example.shelter.common.dto.ListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SidoController {

    private final SidoService sidoService;

    @GetMapping(value = "/api/sidos")
    public ResponseEntity<ListDto<AreaSimpleDto>> getSidos() {
        List<Sido> sidos = sidoService.findAll();
        sidos.sort(Comparator.comparing(Sido::getId));
        List<AreaSimpleDto> simpleDtoList = sidos.stream()
                .map(AreaSimpleDto::ofSido)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ListDto<>(simpleDtoList));
    }

}
