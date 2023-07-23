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
        if (parts.length < 3) {
            return new Address("", "", "", "");
        }
        List<String> partList = new ArrayList<>(Arrays.asList(parts));
        if (partList.get(0).equals("부산")) {
            partList.set(0, "부산광역시");
        }

        if (partList.get(0).equals("세종특별자치시") && !partList.get(1).startsWith("세종")) {
            partList.add(1, "세종시");
        }

        if (partList.get(1).equals("포항시남구")) {
            partList.set(1, "포항시");
            partList.add(2, "남구");
        }


        if (partList.get(0).equals("경기도")) {
            if (partList.get(1).equals("용인시") && partList.get(2).equals("처인구") && partList.get(3).equals("남사면")) {
                partList.set(3, "남사읍");
            }
            else if (partList.get(1).equals("평택시") && partList.get(2).equals("청북면")) {
                partList.set(2, "청북읍");
            }
            else if (partList.get(1).equals("여주시") && partList.get(2).equals("능서면")) {
                partList.set(2, "세종대왕면");
            }
        }

        if(partList.get(0).equals("강원특별자치도")){
            if (partList.get(1).equals("속초시") && partList.get(2).equals("속초시")) {
                partList.remove(2);
            }
            else if (partList.get(1).equals("삼척시") && partList.get(2).equals("정라동")) {
                partList.add(2, "정하동");
            }
        }

        if (partList.get(0).equals("경상북도")) {
            if (partList.get(1).equals("포항시") && partList.get(2).equals("남구") && partList.get(3).equals("대보면")) {
                partList.set(3, "호미곶면");
            }
            else if (partList.get(1).equals("경산시") && partList.get(2).equals("압량면")) {
                partList.set(2, "압량읍");
            }
            else if (partList.get(1).equals("구미시") && partList.get(2).equals("산동면")) {
                partList.set(2, "산동읍");
            }
            else if (partList.get(1).equals("경주시") && partList.get(2).equals("양북면")) {
                partList.set(2, "문무대왕면");
            }
            else if (partList.get(1).equals("상주시") && partList.get(2).equals("사벌면")) {
                partList.set(2, "사벌국면");
            }
            else if (partList.get(1).equals("청송군") && partList.get(2).equals("부동면")) {
                partList.set(2, "주왕산면");
            }
            else if (partList.get(1).equals("군위군")) {
                partList.set(0, "대구광역시");
            }
        }

        if (partList.get(0).equals("충청북도")) {
            if (partList.get(1).equals("진천군") && partList.get(2).equals("덕산면")) {
                partList.set(2, "덕산읍");
            }
        }
        
        if (partList.get(0).equals("전라북도")) {
            if (partList.get(1).equals("전주시") && partList.get(2).equals("덕진구") && partList.get(3).equals("동산동")) {
                partList.set(3, "여의동");
            }
        }

        if (partList.get(0).equals("전라남도")) {
            if (partList.get(1).equals("화순군") && partList.get(2).equals("북면")) {
                partList.set(2, "백아면");
            }
            else if (partList.get(1).equals("화순군") && partList.get(2).equals("남면")) {
                partList.set(2, "사평면");
            }
        }

        if (partList.get(0).equals("울산광역시")) {
            if (partList.get(1).equals("울주군") && partList.get(2).equals("청량면")) {
                partList.set(2, "청량읍");
            }
        }

        if (partList.get(0).equals("대구광역시")) {
            if (partList.get(1).equals("달성군") && partList.get(2).equals("현풍면")) {
                partList.set(2, "현풍읍");
            }
            else if (partList.get(1).equals("달성군") && partList.get(2).equals("옥포면")) {
                partList.set(2, "옥포읍");
            }
            else if (partList.get(1).equals("군위군") && partList.get(2).equals("고로면")) {
                partList.set(2, "삼국유사면");
            }
        }

        if (partList.get(0).equals("부산광역시")) {
            if (partList.get(1).equals("기장군") && partList.get(2).equals("일광면")) {
                partList.set(2, "일광읍");
            }
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
