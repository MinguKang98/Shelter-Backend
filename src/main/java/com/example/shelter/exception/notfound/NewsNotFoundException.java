package com.example.shelter.exception.notfound;

import java.util.Map;

public class NewsNotFoundException extends ResourceNotFoundException{

    public NewsNotFoundException() {
        super("106", "존재하지 않는 뉴스입니다.");
    }

    public NewsNotFoundException(Long id) {
        super("106", "존재하지 않는 뉴스입니다.", Map.of("id", id.toString()));
    }

}
