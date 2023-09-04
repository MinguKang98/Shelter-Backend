package com.example.shelter.sido.dto;

import com.example.shelter.sido.Sido;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SidoListResponse {

    private int count;

    private List<Sido> sidos;

}
