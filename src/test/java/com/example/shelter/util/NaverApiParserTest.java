package com.example.shelter.util;

import com.example.shelter.shelter.address.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class NaverApiParserTest {

    @Autowired
    NaverApiParser naverApiParser;

    @Test
    public void 위경도_주소_가져오기_테스트() {
        //given
        double lat = 37.577400;
        double lon = 127.065355;

        ///when
        ResponseEntity<String> responseEntity = naverApiParser.getResponseEntity(lat, lon);

        //then
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void 위경도_주소_파싱_테스트() {
        //given
        String name = "서울특별시 동대문구 전농동";
        double lat = 37.577400;
        double lon = 127.065355;

        ///when
        Address address = naverApiParser.getAddressByCurrent(lat, lon);

        //then
        assertThat(address.getFullDongName()).isEqualTo(name);
    }

}