package com.example.shelter.shelter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShelterCountDto {

    int tsunami;
    int earthquake;
    int civilDefense;

    protected ShelterCountDto(int tsunami, int earthquake, int civilDefense) {
        this.tsunami = tsunami;
        this.earthquake = earthquake;
        this.civilDefense = civilDefense;
    }

    public static ShelterCountDto of(int tsunami, int earthquake, int civilDefense) {
        return new ShelterCountDto(tsunami, earthquake, civilDefense);
    }
    
}
