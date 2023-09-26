package com.example.shelter.shelter.address;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {

    @Column
    private String sidoName;

    @Column
    private String sigunguName;

    @Column
    private String dongName;

    @Column
    private String detail;

    @Builder
    public Address(String sidoName, String sigunguName, String dongName, String detail) {
        this.sidoName = sidoName;
        this.sigunguName = sigunguName;
        this.dongName = dongName;
        this.detail = detail;
    }

    public String getFullDongName() {
        return String.format("%s %s %s", sidoName, sigunguName, dongName);
    }

    public String getFullAddress() {
        return String.format("%s %s %s %s", sidoName, sigunguName, dongName, detail);
    }

}
