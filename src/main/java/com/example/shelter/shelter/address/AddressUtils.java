package com.example.shelter.shelter.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressUtils {

    private static List<String> exceptSigungus = Arrays.asList(
            "고양시", "성남시", "수원시", "안산시", "안양시",
            "용인시", "전주시", "창원시", "천안시", "청주시"
    );

    public static Address parseAddress(String address) {
        String[] parts = address.split("\\s+");
        List<String> partList = new ArrayList<>(Arrays.asList(parts));
        if (partList.get(0).equals("부산")) {
            partList.set(0, "부산광역시");
        }

        if (partList.get(1).equals("포항시남구")) {
            partList.set(1, "포항시");
            partList.add(2, "남구");
        }

        if (exceptSigungus.contains(partList.get(1))) {
            return Address.builder()
                    .sidoName(partList.get(0))
                    .sigunguName(partList.get(1) + " " + partList.get(2))
                    .dongName(partList.get(3))
                    .detail(String.join(" ", partList.subList(4, partList.size())))
                    .build();
        } else {
            return Address.builder()
                    .sidoName(partList.get(0))
                    .sigunguName(partList.get(1))
                    .dongName(partList.get(2))
                    .detail(String.join(" ", partList.subList(3, partList.size())))
                    .build();
        }
    }

//        String address1 = "경기도 성남시 분당구 정자동   178-5";
//        String address2 = "경기도 남양주시 별내면 청학리 408-2";
//        String address3 = "부산 기장군 일광면 이화로 47-10";
//        String address4 = "부산 포항시남구 동해면 입암리 425-1";

}
