package com.example.shelter.exception.notfound;

import java.util.Map;

public class CivilDefenseShelterNotFoundException extends ResourceNotFoundException {

    public CivilDefenseShelterNotFoundException() {
        super("105", "존재하지 않는 민방위대피소입니다.");
    }

    public CivilDefenseShelterNotFoundException(Long id) {
        super("105", "존재하지 않는 민방위대피소입니다.", Map.of("id", id.toString()));
    }

}
