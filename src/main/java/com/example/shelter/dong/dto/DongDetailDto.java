package com.example.shelter.dong.dto;

import com.example.shelter.common.dto.AreaSimpleDto;
import com.example.shelter.dong.Dong;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DongDetailDto {

    private AreaSimpleDto sido;
    private AreaSimpleDto sigungu;
    private AreaSimpleDto dong;

    public DongDetailDto(Sido sido, Sigungu sigungu, Dong dong) {
        this.sido = AreaSimpleDto.ofSido(sido);
        this.sigungu = AreaSimpleDto.ofSigungu(sigungu);
        this.dong = AreaSimpleDto.ofDong(dong);
    }

}
