package com.example.shelter.shelter.address;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Address {

    @Column
    private String sido;

    @Column
    private String sigungu;

    @Column
    private String dong;

    @Column
    private String detail;

    @Builder
    public Address(String sido, String sigungu, String dong, String detail) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.dong = dong;
        this.detail = detail;
    }

    public String getFullAddress() {
        return String.format("%s %s %s %s", sido, sigungu, dong, detail);
    }

}
