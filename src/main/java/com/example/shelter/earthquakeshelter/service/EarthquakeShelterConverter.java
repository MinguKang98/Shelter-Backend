package com.example.shelter.earthquakeshelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongRepository;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.dto.RawEarthquakeShelter;
import com.example.shelter.earthquakeshelter.parser.RawEarthquakeShelterParser;
import com.example.shelter.earthquakeshelter.repository.EarthquakeShelterRepository;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.shelter.address.AddressUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EarthquakeShelterConverter {

    private final EarthquakeShelterRepository earthquakeShelterRepository;
    private final RawEarthquakeShelterParser rawEarthquakeShelterParser;
    private final DongRepository dongRepository;
    private final int MAX_ROWS = 1000;

    @Transactional
    public void convert() {
        int totalCount = rawEarthquakeShelterParser.getTotalCount();

        int totalPage = totalCount / MAX_ROWS + 1;
        for (int i = 1; i <= totalPage; i++) {
            List<RawEarthquakeShelter> rawEarthquakeShelters = rawEarthquakeShelterParser.parse(i, MAX_ROWS);
            List<EarthquakeShelter> earthquakeShelters = rawEarthquakeShelters.stream()
                    .map(res -> {
                                Address address = AddressUtils.parseAddress(res.getDtlAdres());
                                Dong dong = dongRepository.findByAddressNames(
                                                address.getSidoName(),
                                                address.getSigunguName(),
                                                address.getDongName())
                                        .orElseGet(() -> {
                                            log.error("지진옥외 대피소 동 매핑 실패 - 파싱 시도, 시군구, 읍면동 : {},{},{} 실제 주소 :{}",
                                                    address.getSidoName(), address.getSigunguName(),
                                                    address.getDongName(), res.getDtlAdres());
                                            return null;
                                        });

                                return EarthquakeShelter.builder()
                                        .name(res.getVtAcmdfcltyNm())
                                        .address(address)
                                        .latitude(Double.parseDouble(String.format("%.6f", res.getXCord())))
                                        .longitude(Double.parseDouble(String.format("%.6f", res.getYCord())))
                                        .dong(dong)
                                        .area(res.getFcltyAr())
                                        .build();
                            }
                    )
                    .collect(Collectors.toList());

            earthquakeShelterRepository.saveAll(earthquakeShelters);
        }

    }

}
