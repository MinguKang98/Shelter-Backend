package com.example.shelter.dong;

import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoRepository;
import com.example.shelter.sigungu.Sigungu;
import com.example.shelter.sigungu.SigunguRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressParser {

    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;
    private final DongRepository dongRepository;
    private final String DONG_REGEX = "^.*(읍|면|동|가|로)$";
    private final String CSV_PATH = "csv/address_202201.csv";

    public void parse() {
        Map<String, Sido> sidoMap = new HashMap<>();
        Map<Long, Map<String, Sigungu>> sigunguMap = new HashMap<>();
        Map<Long, Map<String, Dong>> dongMap = new HashMap<>();

        ClassPathResource resource = new ClassPathResource(CSV_PATH);
        String[] areaInfo;

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            while ((areaInfo = csvReader.readNext()) != null) {

                String sidoName = areaInfo[1];
                if (!sidoMap.containsKey(sidoName)) {
                    Sido sido = sidoRepository.findByNameNotDeleted(sidoName)
                            .orElseGet(() -> sidoRepository.save(
                                    Sido.builder()
                                            .name(sidoName)
                                            .build()
                            ));
                    sidoMap.put(sidoName, sido);
                }

                Sido sido = sidoMap.get(sidoName);
                Long sidoId = sido.getId();
                String sigunguName = areaInfo[2];
                if (!sigunguMap.containsKey(sidoId)) {
                    sigunguMap.put(sidoId, new HashMap<>());
                }
                if (!sigunguMap.get(sidoId).containsKey(sigunguName)) {
                    Sigungu guArea = sigunguRepository.findBySidoAndNameNotDeleted(sido, sigunguName)
                            .orElseGet(() -> sigunguRepository.save(
                                    Sigungu.builder()
                                            .name(sigunguName)
                                            .sido(sido)
                                            .build()
                            ));
                    sigunguMap.get(sidoId).put(sigunguName, guArea);
                }

                Sigungu Sigungu = sigunguMap.get(sidoId).get(sigunguName);
                Long guId = Sigungu.getId();
                String dongName = areaInfo[3];
                if (!dongMap.containsKey(guId)) {
                    dongMap.put(guId, new HashMap<>());
                }
                if (!dongMap.get(guId).containsKey(dongName) && dongName.matches(DONG_REGEX)) {
                    Dong dong = dongRepository.findBySigunguAndNameNotDeleted(Sigungu, dongName)
                            .orElseGet(() -> dongRepository.save(
                                    Dong.builder()
                                            .name(dongName)
                                            .sigungu(Sigungu)
                                            .build()));
                    dongMap.get(guId).put(dongName, dong);
                }
            }
        } catch (IOException e) {
            log.error("csv read exception : {}", e);
        }

    }

}
