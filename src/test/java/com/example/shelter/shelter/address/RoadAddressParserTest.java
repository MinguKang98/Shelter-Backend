package com.example.shelter.shelter.address;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoadAddressParserTest {

    @Spy
    RestTemplate restTemplate;

    @InjectMocks
    RoadAddressParser roadAddressParser;

    @Test
    public void 도로명_주소_테스트() {
        //given
        String fullAddr = "서울특별시 동대문구 전농동 산32-20";
        String roadAddress = "서울특별시 동대문구 사가정로 191-1";

        ///when
        String result = roadAddressParser.getRoadAddress(fullAddr);

        //then
        assertThat(result).isEqualTo(roadAddress);
    }

}