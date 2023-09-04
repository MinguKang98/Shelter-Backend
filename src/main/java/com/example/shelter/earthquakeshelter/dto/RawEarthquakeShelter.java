package com.example.shelter.earthquakeshelter.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RawEarthquakeShelter {

    long arcd; // 지역코드

    long acmdfcltySn; // 시설일련번호

    String ctprvnNm; // 시도명

    String sggNm; // 시군구명

    String vtAcmdfcltyNm; // 시설명

    String rdnmadrCd; // 도로명주소코드

    long bdongCd; // 법정동코드

    long hdongCd; // 행정동코드

    String dtlAdres; // 상세주소

    int fcltyAr; // 시설면적

    double xCord; // 경도

    double yCord; // 위도

    @Builder
    public RawEarthquakeShelter(long arcd, long acmdfcltySn, String ctprvnNm, String sggNm,
                                String vtAcmdfcltyNm, String rdnmadrCd, long bdongCd, long hdongCd,
                                String dtlAdres, int fcltyAr, double xCord, double yCord) {
        this.arcd = arcd;
        this.acmdfcltySn = acmdfcltySn;
        this.ctprvnNm = ctprvnNm;
        this.sggNm = sggNm;
        this.vtAcmdfcltyNm = vtAcmdfcltyNm;
        this.rdnmadrCd = rdnmadrCd;
        this.bdongCd = bdongCd;
        this.hdongCd = hdongCd;
        this.dtlAdres = dtlAdres;
        this.fcltyAr = fcltyAr;
        this.xCord = xCord;
        this.yCord = yCord;
    }

}
