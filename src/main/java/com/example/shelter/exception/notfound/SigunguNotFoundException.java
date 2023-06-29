package com.example.shelter.exception.notfound;

import java.util.Map;

public class SigunguNotFoundException extends ResourceNotFoundException{

    public SigunguNotFoundException() {
        super("101", "존재하지 않는 시/군/구입니다.");
    }

    public SigunguNotFoundException(Long id) {
        super("101", "존재하지 않는 시/군/구입니다.", Map.of("id", id.toString()));
    }

    public SigunguNotFoundException(String name) {
        super("101", "존재하지 않는 시/군/구입니다.", Map.of("name", name));
    }

}
