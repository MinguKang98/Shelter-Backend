package com.example.shelter.tsunamishelter.controller;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
import com.example.shelter.exception.notfound.DongNotFoundException;
import com.example.shelter.exception.notfound.TsunamiShelterNotFoundException;
import com.example.shelter.shelter.ShelterVariable;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.service.TsunamiShelterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TsunamiShelterController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TsunamiShelterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TsunamiShelterService tsunamiShelterService;

    @MockBean
    DongService dongService;

    @Test
    public void getTsunamiShelter_존재하는_지진대피소_테스트() throws Exception {
        //given
        Long id = 1L;
        TsunamiShelter shelter = TsunamiShelter.builder()
                .id(id)
                .name("대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(null)
                .capacity(100)
                .height(10)
                .length(10)
                .type("학교")
                .build();
        when(tsunamiShelterService.findById(id)).thenReturn(shelter);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shelter.getId()))
                .andExpect(jsonPath("$.name").value(shelter.getName()))
                .andExpect(jsonPath("$.fullAddress").value(shelter.getAddress().getFullAddress()))
                .andExpect(jsonPath("$.latitude").value(shelter.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(shelter.getLongitude()))
                .andExpect(jsonPath("$.capacity").value(shelter.getCapacity()))
                .andExpect(jsonPath("$.length").value(shelter.getLength()))
                .andExpect(jsonPath("$.height").value(shelter.getHeight()))
                .andExpect(jsonPath("$.type").value(shelter.getType()));
    }

    @Test
    public void getTsunamiShelter_존재하지_않는_지진대피소_테스트() throws Exception {
        //given
        Long id = 1L;
        when(tsunamiShelterService.findById(id)).thenThrow(new TsunamiShelterNotFoundException(id));

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami/{id}", id)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("103"));
    }

    @Test
    public void getTsunamiSheltersByDong_존재하는_동_테스트() throws Exception {
        //given
        Long id = 1L;
        Dong dong = Dong.builder().id(id)
                .name("전농동")
                .sigungu(null)
                .build();
        int page = 1;
        long total = 100;
        PageRequest pageRequest = PageRequest.of(page - 1, ShelterVariable.PAGE_SIZE);
        List<TsunamiShelter> content = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            TsunamiShelter shelter = TsunamiShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            content.add(shelter);
        }
        Page<TsunamiShelter> result = new PageImpl<>(content, pageRequest, total);
        when(dongService.findById(id)).thenReturn(dong);
        when(tsunamiShelterService.findAllByDong(dong, pageRequest)).thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami")
                        .param("dong_id", id.toString())
                        .param("page", String.valueOf(page))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(result.getTotalElements()))
                .andExpect(jsonPath("$.totalPage").value(result.getTotalPages()))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(ShelterVariable.PAGE_SIZE))
                .andExpect(jsonPath("$.hasNext").value(result.hasNext()))
                .andExpect(jsonPath("$.hasPrevious").value(result.hasPrevious()));
    }

    @Test
    public void getTsunamiSheltersByDong_존재하지_않는_동_테스트() throws Exception {
        //given
        Long id = 1L;
        int page = 1;
        when(dongService.findById(id)).thenThrow(new DongNotFoundException(id));

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami")
                        .param("dong_id", id.toString())
                        .param("page", String.valueOf(page))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("102"));
    }

    @Test
    public void getTsunamiSheltersByDong_잘못된_쿼리값_테스트() throws Exception {
        //given
        Long id = 1L;
        int page = 0;
        when(dongService.findById(id)).thenThrow(new TsunamiShelterNotFoundException(id));

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami")
                        .param("dong_id", id.toString())
                        .param("page", String.valueOf(page))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("000"));
    }

    @Test
    public void getTsunamiSheltersByCurrent_기본_반경_테스트() throws Exception {
        //given
        int total = 30;
        double lat = 37.123000;
        double lon = 127.123000;
        List<TsunamiShelter> result = new ArrayList<>();
        for (int i = total; i > 0; i--) {
            TsunamiShelter shelter = TsunamiShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(null)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            result.add(shelter);
        }
        when(tsunamiShelterService.findAllByCurrent(lat, lon, ShelterVariable.DEFAULT_RADIUS))
                .thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami/current")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(total));
    }

    @Test
    public void getTsunamiSheltersByCurrent_반경_입력_테스트() throws Exception {
        //given
        int radius = 200;
        int total = 30;
        double lat = 37.123000;
        double lon = 127.123000;
        List<TsunamiShelter> result = new ArrayList<>();
        for (int i = total; i > 0; i--) {
            TsunamiShelter shelter = TsunamiShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(null)
                    .capacity(100)
                    .height(10)
                    .length(10)
                    .type("학교")
                    .build();
            result.add(shelter);
        }
        when(tsunamiShelterService.findAllByCurrent(lat, lon, radius))
                .thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/tsunami/current")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))
                        .param("radius", String.valueOf(radius))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(total));
    }


}