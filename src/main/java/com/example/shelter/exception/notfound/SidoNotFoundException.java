package com.example.shelter.exception.notfound;

import java.util.Map;

public class SidoNotFoundException extends ResourceNotFoundException {

    public SidoNotFoundException() {
        super("100", "존재하지 않는 시/도입니다.");
    }

    public SidoNotFoundException(Long id) {
        super("100", "존재하지 않는 시/도입니다.", Map.of("id", id.toString()));
    }

    public SidoNotFoundException(String name) {
        super("100", "존재하지 않는 시/도입니다.", Map.of("name", name));
    }

}
