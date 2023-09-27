package com.example.shelter.util;

import com.example.shelter.shelter.address.Address;
import com.example.shelter.shelter.address.AddressUtils;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@RequiredArgsConstructor
public class NaverApiParser {

    private final RestTemplate restTemplate;
    private final String URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    @Value("${apis.naver.client_id}")
    private String NAVER_CLIENT_ID;
    @Value("${apis.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    public Address getAddressByCurrent(double curLat, double curLon) {

        ResponseEntity<String> responseEntity = getResponseEntity(curLat, curLon);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException();
        }

        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(responseEntity.getBody());
            JSONArray jsonArray = (JSONArray) json.get("results");

            JSONObject row0 = (JSONObject) jsonArray.get(0);
            JSONObject region = (JSONObject) row0.get("region");
            JSONObject area1 = (JSONObject) region.get("area1");
            JSONObject area2 = (JSONObject) region.get("area2");
            JSONObject area3 = (JSONObject) region.get("area3");

            String sido = String.valueOf(area1.get("name"));
            String sigungu = String.valueOf(area2.get("name"));
            String dong = String.valueOf(area3.get("name"));

            if (sido.equals("세종특별자치시") && sigungu.isEmpty()) {
                sigungu = "세종시";
            }

            return AddressUtils.parseAddress(String.format("%s %s %s", sido, sigungu, dong));
        } catch (ParseException e) {
            throw new RuntimeException();
        }

    }


    public ResponseEntity<String> getResponseEntity(double curLat, double curLon) {

        try {
            String url = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("coords", String.format("%f,%f", curLon, curLat))
                    .queryParam("output", "json")
                    .build()
                    .toUriString();
            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-NCP-APIGW-API-KEY-ID", NAVER_CLIENT_ID);
            headers.add("X-NCP-APIGW-API-KEY", NAVER_CLIENT_SECRET);

            return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

}
