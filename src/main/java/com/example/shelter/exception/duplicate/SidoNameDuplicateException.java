package com.example.shelter.exception.duplicate;

import java.util.Map;

public class SidoNameDuplicateException extends DuplicateException{

    public SidoNameDuplicateException(String name) {
        super("200", "이미 존재하는 시/도 이름입니다.", Map.of("sidoName", name));
    }

}
