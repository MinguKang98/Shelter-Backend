package com.example.shelter.civildefenseshelter.parser;

import com.example.shelter.civildefenseshelter.dto.RawCivilDefenseShelter;
import com.example.shelter.util.TransCoord;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RawCivilDefenseShelterParser {

    private final String CSV_PATH = "csv/civil_defense_shelter_20221020.csv";

    public List<RawCivilDefenseShelter> parse() {
        ClassPathResource resource = new ClassPathResource(CSV_PATH);
        String[] areaInfo;
        List<RawCivilDefenseShelter> rawCivilDefenseShelterList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            csvReader.readNext();
            while ((areaInfo = csvReader.readNext()) != null) {
                int status = Integer.parseInt(areaInfo[7]);
                if (status == 4) {
                    break;
                }
                double latitude;
                double longitude;

                double area = Double.parseDouble(areaInfo[16]);
                String fullAddress = areaInfo[18];
                String roadAddress = areaInfo[19];
                String name = areaInfo[21];
                String coordType = areaInfo[26];
                String strX = areaInfo[27];
                String strY = areaInfo[28];

                if ("EPSG:5174".equals(coordType)) {
                    ProjCoordinate coordinate = TransCoord.transform(strX, strY);
                    latitude = coordinate.y;
                    longitude = coordinate.x;
                } else {
                    latitude = Double.parseDouble(strY);
                    longitude = Double.parseDouble(strX);
                }

                String type = areaInfo[30];

                RawCivilDefenseShelter rawCivilDefenseShelter = RawCivilDefenseShelter.builder()
                        .area(area)
                        .fullAddress(fullAddress)
                        .roadAddress(roadAddress)
                        .name(name)
                        .coordType(coordType)
                        .latitude(latitude)
                        .longitude(longitude)
                        .type(type)
                        .build();

                rawCivilDefenseShelterList.add(rawCivilDefenseShelter);
            }
        } catch (IOException e) {
            log.error("csv read exception : {}", e);
        }

        return rawCivilDefenseShelterList;
    }

}
