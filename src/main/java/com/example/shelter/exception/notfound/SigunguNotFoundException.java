package com.example.shelter.exception.notfound;

public class SigunguNotFoundException extends ResourceNotFoundException{

    public SigunguNotFoundException() {
        super("101", "존재하지 않는 시/군/구입니다.");
    }
}
