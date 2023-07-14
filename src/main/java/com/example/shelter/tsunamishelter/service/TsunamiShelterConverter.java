package com.example.shelter.tsunamishelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongRepository;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.shelter.address.AddressUtils;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.dto.RawTsunamiShelter;
import com.example.shelter.tsunamishelter.parser.RawTsunamiShelterParser;
import com.example.shelter.tsunamishelter.repository.TsunamiShelterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TsunamiShelterConverter {

    private final TsunamiShelterRepository tsunamiShelterRepository;
    private final RawTsunamiShelterParser rawTsunamiShelterParser;
    private final DongRepository dongRepository;
    private final int MAX_ROWS = 1000;

    public void convert() {
        int totalCount = rawTsunamiShelterParser.getTotalCount();

        int totalPage = totalCount / MAX_ROWS + 1;
        for (int i = 1; i <= totalPage; i++) {
            List<RawTsunamiShelter> rawTsunamiShelters = rawTsunamiShelterParser.parse(i, MAX_ROWS);
            List<TsunamiShelter> tsunamiShelters = rawTsunamiShelters.stream()
                    .map(rts -> {
                                Address address = AddressUtils.parseAddress(rts.getAddress());
                                Dong dong = dongRepository.findByAddressNames(
                                                address.getSidoName(),
                                                address.getSigunguName(),
                                                address.getDongName())
                                        .orElseGet(() -> {
                                            log.error("지진해일 대피소 동 매핑 실패 - 파싱 시도, 시군구, 읍면동 : {},{},{} 실제 주소 :{}",
                                                    address.getSidoName(), address.getSigunguName(),
                                                    address.getDongName(), rts.getAddress());
                                            return null;
                                        });

                                return TsunamiShelter.builder()
                                        .name(rts.getShelNm())
                                        .address(address)
                                        .latitude(Double.parseDouble(String.format("%.6f", rts.getLat())))
                                        .longitude(Double.parseDouble(String.format("%.6f", rts.getLon())))
                                        .dong(dong)
                                        .capacity(rts.getShelAv())
                                        .height(rts.getHeight())
                                        .length(rts.getLength())
                                        .type(rts.getShelDivType())
                                        .build();
                            }
                    )
                    .collect(Collectors.toList());

            tsunamiShelterRepository.saveAll(tsunamiShelters);
        }
    }

}
