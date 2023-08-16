package com.example.shelter.exception.notfound;

import java.util.Map;

public class ShelterTypeNotFoundException extends ResourceNotFoundException {

    public ShelterTypeNotFoundException() {
        super("107", "존재하지 않는 대피소 종류입니다.");
    }

    public ShelterTypeNotFoundException(String type) {
        super("107", "존재하지 않는 대피소 종류입니다.", Map.of("type", type));
    }

}
