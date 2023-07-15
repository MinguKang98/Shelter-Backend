package com.example.shelter.earthquake.parser;

import com.example.shelter.earthquake.dto.RawEarthquakeShelter;
import com.example.shelter.exception.badinput.NumberOfRowsException;
import com.example.shelter.exception.badinput.PageNumberException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RawEarthquakeShelterParser {

    private final RestTemplate restTemplate;
    private final String URL = "http://apis.data.go.kr/1741000/EmergencyAssemblyArea_Earthquake2/getArea1List";
    private final String SERVICE_KEY = "%2BeuqeRinZwJwov6vl5hsAHg3NBJ3smIJoCQDEx0lUuDQuhRafmQ9yS%2B1vpKrAdmV%2BG9j6p%2FqfsPZlTpTkb95%2Bw%3D%3D";

    public int getTotalCount() {

        ResponseEntity<String> result = getResponseEntity(1, 1);

        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result.getBody());
            JSONArray jsonArray = (JSONArray) json.get("EarthquakeOutdoorsShelter");

            // head
            JSONObject headJson = (JSONObject) jsonArray.get(0);
            JSONArray headJsonArray = (JSONArray) headJson.get("head");
            JSONObject totalCountObject = (JSONObject) headJsonArray.get(0);

            return Integer.parseInt(String.valueOf(totalCountObject.get("totalCount")));
        } catch (ParseException e) {
            throw new RuntimeException();
        }

    }

    public List<RawEarthquakeShelter> parse(int pageNo, int numberOfRows) {

        List<RawEarthquakeShelter> rawEarthquakeShelterList = new ArrayList<>();

        try {
            ResponseEntity<String> result = getResponseEntity(pageNo, numberOfRows);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result.getBody());
            JSONArray jsonArray = (JSONArray) json.get("EarthquakeOutdoorsShelter");

            // row
            JSONObject rowJson = (JSONObject) jsonArray.get(1);
            JSONArray rowJsonArray = (JSONArray) rowJson.get("row");
            for (Object o : rowJsonArray) {
                JSONObject rowObject = (JSONObject) o;
                long arcd = Long.parseLong(String.valueOf(rowObject.get("arcd")));
                long acmdfcltySn = Long.parseLong(String.valueOf(rowObject.get("acmdfclty_sn")));
                String ctprvnNm = String.valueOf(rowObject.get("ctprvn_nm"));
                String sggNm = String.valueOf(rowObject.get("sgg_nm"));
                String vtAcmdfcltyNm = String.valueOf(rowObject.get("vt_acmdfclty_nm"));
                String rdnmadrCd = String.valueOf(rowObject.get("rdnmadr_cd"));
                long bdongCd = Long.parseLong(String.valueOf(rowObject.get("bdong_cd")));
                long hdongCd = Long.parseLong(String.valueOf(rowObject.get("hdong_cd")));
                String dtlAdres = String.valueOf(rowObject.get("dtl_adres"));
                int fcltyAr = Integer.parseInt(String.valueOf(rowObject.get("fclty_ar")));
                double xCord = Double.parseDouble(String.valueOf(rowObject.get("xcord")));
                double yCord = Double.parseDouble(String.valueOf(rowObject.get("ycord")));

                RawEarthquakeShelter rawData = RawEarthquakeShelter.builder()
                        .arcd(arcd)
                        .acmdfcltySn(acmdfcltySn)
                        .ctprvnNm(ctprvnNm)
                        .sggNm(sggNm)
                        .vtAcmdfcltyNm(vtAcmdfcltyNm)
                        .rdnmadrCd(rdnmadrCd)
                        .bdongCd(bdongCd)
                        .hdongCd(hdongCd)
                        .dtlAdres(dtlAdres)
                        .fcltyAr(fcltyAr)
                        .xCord(xCord)
                        .yCord(yCord)
                        .build();
                rawEarthquakeShelterList.add(rawData);
            }
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        return rawEarthquakeShelterList;

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
