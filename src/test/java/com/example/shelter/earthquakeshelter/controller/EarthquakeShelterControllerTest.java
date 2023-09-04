package com.example.shelter.earthquakeshelter.controller;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.service.EarthquakeShelterService;
import com.example.shelter.exception.notfound.DongNotFoundException;
import com.example.shelter.exception.notfound.EarthquakeShelterNotFoundException;
import com.example.shelter.shelter.ShelterVariable;
import com.example.shelter.shelter.address.Address;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EarthquakeShelterController.class)
@MockBean(JpaMetamodelMappingContext.class)
class EarthquakeShelterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EarthquakeShelterService earthquakeShelterService;

    @MockBean
    DongService dongService;

    @Test
    public void getEarthquakeShelter_존재하는_지진대피소_테스트() throws Exception {
        //given
        Long id = 1L;
        EarthquakeShelter shelter = EarthquakeShelter.builder()
                .id(id)
                .name("대피소")
                .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                .latitude(37.123456)
                .longitude(127.123456)
                .dong(null)
                .area(1400)
                .build();
        when(earthquakeShelterService.findById(id)).thenReturn(shelter);

        ///when
        //then
        mockMvc.perform(get("/api/shelters/earthquake/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shelter.getId()))
                .andExpect(jsonPath("$.name").value(shelter.getName()))
                .andExpect(jsonPath("$.fullAddress").value(shelter.getAddress().getFullAddress()))
                .andExpect(jsonPath("$.latitude").value(shelter.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(shelter.getLongitude()))
                .andExpect(jsonPath("$.area").value(shelter.getArea()));
    }

    @Test
    public void getEarthquakeShelter_존재하지_않는_지진대피소_테스트() throws Exception {
        //given
        Long id = 1L;
        when(earthquakeShelterService.findById(id)).thenThrow(new EarthquakeShelterNotFoundException());

        ///when
        //then
        mockMvc.perform(get("/api/shelters/earthquake/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("104"));
    }

    @Test
    public void getEarthquakeSheltersByDong_존재하는_동_테스트() throws Exception {
        //given
        Long id = 1L;
        Dong dong = Dong.builder().id(id)
                .name("전농동")
                .sigungu(null)
                .build();

        int page = 1;
        long total = 100;
        PageRequest pageRequest = PageRequest.of(page - 1, ShelterVariable.PAGE_SIZE);
        List<EarthquakeShelter> content = new ArrayList<>();
        for (int i = ShelterVariable.PAGE_SIZE; i > 0; i--) {
            EarthquakeShelter shelter = EarthquakeShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(dong)
                    .area(1400)
                    .build();
            content.add(shelter);
        }
        Page<EarthquakeShelter> result = new PageImpl<>(content, pageRequest, total);
        when(dongService.findById(id)).thenReturn(dong);
        when(earthquakeShelterService.findAllByDong(dong, pageRequest)).thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/earthquake")
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
    public void getEarthquakeSheltersByDong_존재하지_않는_동_테스트() throws Exception {
        //given
        Long id = 1L;
        int page = 1;
        when(dongService.findById(id)).thenThrow(new DongNotFoundException(id));

        ///when
        //then
        mockMvc.perform((get("/api/shelters/earthquake")
                        .param("dong_id", id.toString())
                        .param("page", String.valueOf(page))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("102"));
    }

    @Test
    public void getEarthquakeSheltersByDong_잘못된_쿼리값_테스트() throws Exception {
        Long id = 1L;
        int page = 0;

        ///when
        //then
        mockMvc.perform((get("/api/shelters/earthquake")
                        .param("dong_id", id.toString())
                        .param("page", String.valueOf(page))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("000"));
    }

    @Test
    public void getEarthquakeSheltersByCurrent_기본_반경_테스트() throws Exception {
        //given
        int total = 30;
        double lat = 37.123000;
        double lon = 127.123000;
        List<EarthquakeShelter> result = new ArrayList<>();
        for (int i = total; i > 0; i--) {
            EarthquakeShelter shelter = EarthquakeShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(null)
                    .area(1400)
                    .build();
            result.add(shelter);
        }
        when(earthquakeShelterService.findAllByCurrent(lat, lon, ShelterVariable.DEFAULT_RADIUS))
                .thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/earthquake/current")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(total));
    }

    @Test
    public void getEarthquakeSheltersByCurrent_반경_입력_테스트() throws Exception {
        //given
        int radius = 200;
        int total = 30;
        double lat = 37.123000;
        double lon = 127.123000;
        List<EarthquakeShelter> result = new ArrayList<>();
        for (int i = total; i > 0; i--) {
            EarthquakeShelter shelter = EarthquakeShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(null)
                    .area(1400)
                    .build();
            result.add(shelter);
        }
        when(earthquakeShelterService.findAllByCurrent(lat, lon, radius))
                .thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/earthquake/current")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))
                        .param("radius", String.valueOf(radius))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(total));
    }


}