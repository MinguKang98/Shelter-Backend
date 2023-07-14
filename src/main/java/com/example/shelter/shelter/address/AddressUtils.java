package com.example.shelter.shelter.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressUtils {

    private static List<String> exceptSigungus = Arrays.asList(
            "고양시", "성남시", "수원시", "안산시", "안양시", "용인시",
            "전주시", "창원시", "천안시", "청주시", "포항시"
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

        if (partList.get(1).equals("포항시") && partList.get(2).equals("남구") && partList.get(3).equals("대보면")) {
            partList.set(3, "호미곶면");
        }

        if (partList.get(1).equals("속초시") && partList.get(2).equals("속초시")) {
            partList.remove(2);
        }

        if (partList.get(1).equals("삼척시") && partList.get(2).equals("정라동")) {
            partList.add(2, "정하동");
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

}
