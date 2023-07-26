package com.example.shelter.civildefenseshelter.service;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.civildefenseshelter.dto.RawCivilDefenseShelter;
import com.example.shelter.civildefenseshelter.parser.RawCivilDefenseShelterParser;
import com.example.shelter.civildefenseshelter.repository.CivilDefenseShelterRepository;
import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongRepository;
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
public class CivilDefenseShelterConverter {

    private final CivilDefenseShelterRepository civilDefenseShelterRepository;
    private final RawCivilDefenseShelterParser rawCivilDefenseShelterParser;
    private final DongRepository dongRepository;

    @Transactional
    public void convert() {

        List<RawCivilDefenseShelter> rawCivilDefenseShelters = rawCivilDefenseShelterParser.parse();
        List<CivilDefenseShelter> civilDefenseShelters = rawCivilDefenseShelters.stream()
                .map(rcs -> {
                            Address address = AddressUtils.parseAddress(rcs.getFullAddress());
                            Dong dong = dongRepository.findByAddressNames(
                                            address.getSidoName(),
                                            address.getSigunguName(),
                                            address.getDongName())
                                    .orElseGet(() -> {
                                        log.error("민방위 대피소 동 매핑 실패 - 파싱 시도, 시군구, 읍면동 : {},{},{} 실제 주소 :{}",
                                                address.getSidoName(), address.getSigunguName(),
                                                address.getDongName(), rcs.getFullAddress());
                                        return null;
                                    });

                            return CivilDefenseShelter.builder()
                                    .name(rcs.getName())
                                    .address(address)
                                    .roadAddress(rcs.getRoadAddress())
                                    .latitude(Double.parseDouble(String.format("%.6f", rcs.getLatitude())))
                                    .longitude(Double.parseDouble(String.format("%.6f", rcs.getLongitude())))
                                    .dong(dong)
                                    .area(rcs.getArea())
                                    .type(rcs.getType())
                                    .build();
                        }
                )
                .collect(Collectors.toList());
        civilDefenseShelterRepository.saveAll(civilDefenseShelters);
    }

}
