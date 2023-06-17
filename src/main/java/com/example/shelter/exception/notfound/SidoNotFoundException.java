package com.example.shelter.exception.notfound;

import com.example.shelter.exception.notfound.ResourceNotFoundException;

public class SidoNotFoundException extends ResourceNotFoundException {

    public SidoNotFoundException() {
        super("100", "존재하지 않는 시/도입니다.");
    }
}
