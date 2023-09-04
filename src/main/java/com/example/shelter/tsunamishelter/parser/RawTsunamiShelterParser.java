package com.example.shelter.tsunamishelter.parser;

import com.example.shelter.exception.badinput.NumberOfRowsException;
import com.example.shelter.exception.badinput.PageNumberException;
import com.example.shelter.tsunamishelter.dto.RawTsunamiShelter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RawTsunamiShelterParser {

    private final RestTemplate restTemplate;
    private final String URL = "http://apis.data.go.kr/1741000/TsunamiShelter3/getTsunamiShelter1List";
    @Value("${apis.tsunami_key}")
    private String SERVICE_KEY;

    public int getTotalCount() {

        ResponseEntity<String> result = getResponseEntity(1, 1);

        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result.getBody());
            JSONArray jsonArray = (JSONArray) json.get("TsunamiShelter");

            // head
            JSONObject headJson = (JSONObject) jsonArray.get(0);
            JSONArray headJsonArray = (JSONArray) headJson.get("head");
            JSONObject totalCountObject = (JSONObject) headJsonArray.get(0);

            return Integer.parseInt(String.valueOf(totalCountObject.get("totalCount")));
        } catch (ParseException e) {
            throw new RuntimeException();
        }

    }

    public List<RawTsunamiShelter> parse(int pageNo, int numberOfRows) {

        List<RawTsunamiShelter> rawTsunamiShelterList = new ArrayList<>();

        try {
            ResponseEntity<String> result = getResponseEntity(pageNo, numberOfRows);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result.getBody());
            JSONArray jsonArray = (JSONArray) json.get("TsunamiShelter");

            // row
            JSONObject rowJson = (JSONObject) jsonArray.get(1);
            JSONArray rowJsonArray = (JSONArray) rowJson.get("row");
            for (Object o : rowJsonArray) {
                JSONObject rowObject = (JSONObject) o;
                int id = Integer.parseInt(String.valueOf(rowObject.get("id")));
                String sidoName = String.valueOf(rowObject.get("sido_name"));
                String sigunguName = String.valueOf(rowObject.get("sigungu_name"));
                String remarks = String.valueOf(rowObject.get("remarks"));
                String shelNm = String.valueOf(rowObject.get("shel_nm"));
                String address = String.valueOf(rowObject.get("address"));
                double lon = Double.parseDouble(String.valueOf(rowObject.get("lon")));
                double lat = Double.parseDouble(String.valueOf(rowObject.get("lat")));
                int shelAv = Integer.parseInt(String.valueOf(rowObject.get("shel_av")));
                int length = Integer.parseInt(String.valueOf(rowObject.get("lenth")));
                String shelDivType = String.valueOf(rowObject.get("shel_div_type"));
                String seismic = String.valueOf(rowObject.get("seismic"));
                int height = Integer.parseInt(String.valueOf(rowObject.get("height")));

                RawTsunamiShelter rawData = RawTsunamiShelter.builder()
                        .id(id)
                        .sidoName(sidoName)
                        .sigunguName(sigunguName)
                        .remarks(remarks)
                        .shelNm(shelNm)
                        .address(address)
                        .lon(lon)
                        .lat(lat)
                        .shelAv(shelAv)
                        .length(length)
                        .shelDivType(shelDivType)
                        .seismic(seismic)
                        .height(height)
                        .build();
                rawTsunamiShelterList.add(rawData);
            }
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        return rawTsunamiShelterList;

    }

    public ResponseEntity<String> getResponseEntity(int pageNo, int numberOfRows) {

        if (pageNo < 1) {
            throw new PageNumberException(pageNo);
        }

        if (numberOfRows < 1 || numberOfRows > 1000) {
            throw new NumberOfRowsException(numberOfRows);
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("ServiceKey", SERVICE_KEY)
                    .queryParam("type", "json")
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numberOfRows)
                    .build()
                    .toUriString();
            URI uri = new URI(url);

            return restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }

    }

}