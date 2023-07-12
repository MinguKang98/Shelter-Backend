package com.example.shelter.tsunamishelter.service;

import com.example.shelter.shelter.address.AddressUtils;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.dto.RawTsunamiShelter;
import com.example.shelter.tsunamishelter.parser.RawTsunamiShelterParser;
import com.example.shelter.tsunamishelter.repository.TsunamiShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TsunamiShelterConverter {

    private final TsunamiShelterRepository tsunamiShelterRepository;
    private final RawTsunamiShelterParser rawTsunamiShelterParser;
    private final int MAX_ROWS = 1000;

    public void converter() {
        int totalCount = rawTsunamiShelterParser.getTotalCount();

        int totalPage = totalCount / MAX_ROWS + 1;
        for (int i = 1; i <= totalPage; i++) {
            List<RawTsunamiShelter> rawTsunamiShelters = rawTsunamiShelterParser.parse(i, MAX_ROWS);
            List<TsunamiShelter> tsunamiShelters = rawTsunamiShelters.stream()
                    .map(rts -> TsunamiShelter.builder()
                            .name(rts.getShelNm())
                            .address(AddressUtils.parseAddress(rts.getAddress()))
                            .latitude(Double.parseDouble(String.format("%.6f", rts.getLat())))
                            .longitude(Double.parseDouble(String.format("%.6f", rts.getLon())))
                            .dong(null) // dongRepository 메서드 만든 후 적용
                            .capacity(rts.getShelAv())
                            .height(rts.getHeight())
                            .length(rts.getLength())
                            .type(rts.getShelDivType())
                            .build()
                    )
                    .collect(Collectors.toList());

            tsunamiShelterRepository.saveAll(tsunamiShelters);
        }
    }

}
