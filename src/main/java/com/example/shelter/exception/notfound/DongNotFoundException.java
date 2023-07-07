package com.example.shelter.exception.notfound;

import java.util.Map;

public class DongNotFoundException extends ResourceNotFoundException {

    public DongNotFoundException() {
        super("102", "존재하지 않는 읍/면/동입니다.");
    }

    public DongNotFoundException(Long id) {
        super("102", "존재하지 않는 읍/면/동입니다.", Map.of("id", id.toString()));
    }

    public DongNotFoundException(String name) {
        super("102", "존재하지 않는 읍/면/동입니다.", Map.of("name", name));
    }

}
