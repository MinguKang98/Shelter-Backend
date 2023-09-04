package com.example.shelter.exception.notfound;

import java.util.Map;

public class EarthquakeShelterNotFoundException extends ResourceNotFoundException {

    public EarthquakeShelterNotFoundException() {
        super("104", "존재하지 않는 지진대피소입니다.");
    }

    public EarthquakeShelterNotFoundException(Long id) {
        super("104", "존재하지 않는 지진대피소입니다.", Map.of("id", id.toString()));
    }

}
