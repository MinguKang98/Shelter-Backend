package com.example.shelter.shelter.address;

import com.example.shelter.exception.badinput.NumberOfRowsException;
import com.example.shelter.exception.badinput.PageNumberException;
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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class RoadAddressParser {

    private final RestTemplate restTemplate;
    private final String URL = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
    @Value("${apis.address_search_key}")
    private String SERVICE_KEY;

    public String getRoadAddress(Address address) {
        return getRoadAddress(address.getFullAddress());
    }

    public String getRoadAddress(String fullAddress) {
        if (fullAddress.endsWith("-0")) {
            fullAddress = fullAddress.replace("-0", "");
        }

        ResponseEntity<String> result = getResponseEntity(fullAddress, 1, 10);

        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result.getBody());
            JSONObject results = (JSONObject) json.get("results");
            JSONObject common = (JSONObject) results.get("common");

            int totalCount = Integer.parseInt(String.valueOf(common.get("totalCount")));
            if (totalCount == 0) {
                return "";
            }

            JSONArray juso = (JSONArray) results.get("juso");
            JSONObject firstJuso = (JSONObject) juso.get(0);
            return String.valueOf(firstJuso.get("roadAddrPart1"));
        } catch (ParseException e) {
            return "";
        }
    }


    public ResponseEntity<String> getResponseEntity(String fullAddress, int pageNo, int numberOfRows) {

        if (pageNo < 1) {
            throw new PageNumberException(pageNo);
        }

        if (numberOfRows < 1 || numberOfRows > 100) {
            throw new NumberOfRowsException(numberOfRows);
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("confmKey", SERVICE_KEY)
                    .queryParam("currentPage", pageNo)
                    .queryParam("countPerPage", numberOfRows)
                    .queryParam("hstryYn", "Y")
                    .queryParam("keyword", URLEncoder.encode(fullAddress, "UTF-8"))
                    .queryParam("resultType", "json")
                    .build()
                    .toUriString();
            URI uri = new URI(url);

            return restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

}
