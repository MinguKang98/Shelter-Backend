package com.example.shelter.exception.notfound;

import java.util.Map;

public class TsunamiShelterNotFoundException extends ResourceNotFoundException{

    public TsunamiShelterNotFoundException() {
        super("103", "존재하지 않는 지진해일대피소입니다.");
    }

    public TsunamiShelterNotFoundException(Long id) {
        super("103", "존재하지 않는 지진해일대피소입니다.", Map.of("id", id.toString()));
    }

}
