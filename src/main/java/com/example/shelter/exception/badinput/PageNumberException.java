package com.example.shelter.exception.badinput;

import java.util.Map;

public class PageNumberException extends BadInputException {

    public PageNumberException(int pageNo) {
        super("300", "페이지 번호는 1이상입니다.", Map.of("pageNo", String.valueOf(pageNo)));
    }

}
