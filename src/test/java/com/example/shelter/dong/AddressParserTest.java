package com.example.shelter.dong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressParserTest {

    @Autowired
    AddressParser addressParser;

//    @Test
//    @Rollback(value = false)
    public void parse_테스트() {
        //given

        ///when
        long start = System.currentTimeMillis();
        addressParser.parse();
        long end = System.currentTimeMillis();
        long time = end - start;

        //then
        System.out.println("time = " + time);
    }

}