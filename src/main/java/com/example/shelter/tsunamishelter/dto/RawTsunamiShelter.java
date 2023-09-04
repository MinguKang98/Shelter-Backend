package com.example.shelter.tsunamishelter.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RawTsunamiShelter {

    private int id;

    private String sidoName;

    private String sigunguName;

    private String remarks;

    private String shelNm;

    private String address;

    private double lon;

    private double lat;

    private int shelAv;

    private int length;

    private String shelDivType;

    private String seismic;

    private int height;


    @Builder
    public RawTsunamiShelter(int id, String sidoName, String sigunguName, String remarks,
                             String shelNm, String address, double lon, double lat, int shelAv,
                             int length, String shelDivType, String seismic, int height) {
        this.id = id;
        this.sidoName = sidoName;
        this.sigunguName = sigunguName;
        this.remarks = remarks;
        this.shelNm = shelNm;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
        this.shelAv = shelAv;
        this.length = length;
        this.shelDivType = shelDivType;
        this.seismic = seismic;
        this.height = height;
    }

}
