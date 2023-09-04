package com.example.shelter.common.dto;

import com.example.shelter.dong.Dong;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AreaSimpleDto {

    Long id;

    String name;

    protected AreaSimpleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AreaSimpleDto ofSido(Sido sido) {
        return new AreaSimpleDto(sido.getId(), sido.getName());
    }

    public static AreaSimpleDto ofSigungu(Sigungu sigungu) {
        return new AreaSimpleDto(sigungu.getId(), sigungu.getName());
    }

    public static AreaSimpleDto ofDong(Dong dong) {
        return new AreaSimpleDto(dong.getId(), dong.getName());
    }

}
