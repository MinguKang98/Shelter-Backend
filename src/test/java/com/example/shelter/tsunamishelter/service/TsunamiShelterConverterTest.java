package com.example.shelter.tsunamishelter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class TsunamiShelterConverterTest {

    @Autowired
    TsunamiShelterConverter tsunamiShelterConverter;

//    @Test
//    @Rollback(value = false)
    public void convert_테스트() {
        //given

        ///when
        tsunamiShelterConverter.convert();

        //then
    }
}