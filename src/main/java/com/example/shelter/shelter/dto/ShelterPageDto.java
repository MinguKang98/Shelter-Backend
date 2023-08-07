package com.example.shelter.shelter.dto;

import com.example.shelter.shelter.Shelter;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.dto.TsunamiShelterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ShelterPageDto {

    private long totalCount;

    private int totalPage;

    private int page;

    private int size;

    boolean hasNext;

    boolean hasPrevious;

    private List<?> content;

    protected ShelterPageDto(Page<? extends Shelter> page) {
        this.totalCount = page.getTotalElements();
        this.totalPage = page.getTotalPages();
        this.page = page.getNumber() + 1;
        this.size = page.getNumberOfElements();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.content = page.getContent().stream()
                .map(s ->
                        switch (s.getShelterType()) {
                            case TSUNAMI -> TsunamiShelterDto.of((TsunamiShelter) s);
                            case EARTHQUAKE -> null;
                            case CIVIL_DEFENCE -> null;
                        }
                )
                .collect(Collectors.toList());
    }

    public static ShelterPageDto of(Page<? extends Shelter> page) {
        return new ShelterPageDto(page);
    }

}
