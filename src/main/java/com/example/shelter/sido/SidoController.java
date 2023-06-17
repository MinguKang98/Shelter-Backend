package com.example.shelter.sido;

import com.example.shelter.sido.dto.SidoListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SidoController {

    private final SidoService sidoService;

    @GetMapping(value = "/api/sidos")
    public ResponseEntity<SidoListResponse> getSidos() {
        List<Sido> sidos = sidoService.findAll();
        return ResponseEntity.ok(new SidoListResponse(sidos.size(), sidos));
    }

    @GetMapping(value = "/api/sidos/{id}")
    public ResponseEntity<Sido> getSidoById(@PathVariable("id") Long id) {
        Sido sido = sidoService.findById(id);
        return ResponseEntity.ok(sido);
    }

}
