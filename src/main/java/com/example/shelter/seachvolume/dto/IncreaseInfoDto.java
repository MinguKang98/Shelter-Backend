package com.example.shelter.seachvolume.dto;

import com.example.shelter.shelter.ShelterType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IncreaseInfoDto {

    @Positive
    private Long dongId;

    @NotNull
    private ShelterType type;

}
