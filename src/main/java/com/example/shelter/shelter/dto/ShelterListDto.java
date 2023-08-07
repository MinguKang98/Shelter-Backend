package com.example.shelter.shelter.dto;

import com.example.shelter.shelter.Shelter;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.dto.TsunamiShelterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ShelterListDto {

    private long totalCount;

    private List<?> content;

    protected ShelterListDto(List<? extends Shelter> content) {
        this.totalCount = content.size();
        this.content = content.stream()
                .map(s ->
                        switch (s.getShelterType()) {
                            case TSUNAMI -> TsunamiShelterDto.of((TsunamiShelter) s);
                            case EARTHQUAKE -> null;
                            case CIVIL_DEFENCE -> null;
                        }
                )
                .collect(Collectors.toList());
    }

    public static ShelterListDto of(List<? extends Shelter> content) {
        return new ShelterListDto(content);
    }

}
