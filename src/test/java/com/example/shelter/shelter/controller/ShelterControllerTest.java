package com.example.shelter.shelter.controller;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.civildefenseshelter.service.CivilDefenseShelterService;
import com.example.shelter.earthquakeshelter.service.EarthquakeShelterService;
import com.example.shelter.exception.notfound.SidoNotFoundException;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.ShelterVariable;
import com.example.shelter.shelter.address.Address;
import com.example.shelter.shelter.service.ShelterService;
import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoService;
import com.example.shelter.tsunamishelter.service.TsunamiShelterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShelterController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ShelterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShelterService shelterService;

    @MockBean
    TsunamiShelterService tsunamiShelterService;

    @MockBean
    EarthquakeShelterService earthquakeShelterService;

    @MockBean
    CivilDefenseShelterService civilDefenseShelterService;

    @MockBean
    SidoService sidoService;

    @Test
    public void getShelterCount_전체_대피소_테스트() throws Exception {
        //given
        int tsunami = 10;
        int earthquake = 20;
        int civilDefense = 30;

        when(tsunamiShelterService.countAll()).thenReturn(tsunami);
        when(earthquakeShelterService.countAll()).thenReturn(earthquake);
        when(civilDefenseShelterService.countAll()).thenReturn(civilDefense);

        ///when
        //then
        mockMvc.perform(get("/api/shelters/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tsunami").value(tsunami))
                .andExpect(jsonPath("$.earthquake").value(earthquake))
                .andExpect(jsonPath("$.civilDefense").value(civilDefense));
    }

    @Test
    public void getShelterCount_시도_대피소_테스트() throws Exception {
        //given
        Long id = 1L;
        int tsunami = 10;
        int earthquake = 20;
        int civilDefense = 30;
        Sido sido = Sido.builder()
                .id(id)
                .name("서울특별시")
                .build();

        when(sidoService.findById(id)).thenReturn(sido);
        when(tsunamiShelterService.countAllBySido(sido)).thenReturn(tsunami);
        when(earthquakeShelterService.countAllBySido(sido)).thenReturn(earthquake);
        when(civilDefenseShelterService.countAllBySido(sido)).thenReturn(civilDefense);

        ///when
        //then
        mockMvc.perform(get("/api/shelters/count")
                        .param("sido_id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tsunami").value(tsunami))
                .andExpect(jsonPath("$.earthquake").value(earthquake))
                .andExpect(jsonPath("$.civilDefense").value(civilDefense));
    }

    @Test
    public void getShelterCount_존재하지_않는_시도_테스트() throws Exception {
        //given
        Long id = 1L;
        when(sidoService.findById(id)).thenThrow(new SidoNotFoundException(id));

        ///when
        //then
        mockMvc.perform(get("/api/shelters/count")
                        .param("sido_id", id.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("100"));
    }

    @Test
    public void getSheltersByCurrent_기본_반경_테스트() throws Exception {
        int total = 30;
        double lat = 37.123000;
        double lon = 127.123000;
        List<Shelter> result = new ArrayList<>();
        for (int i = total; i > 0; i--) {
            CivilDefenseShelter shelter = CivilDefenseShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(null)
                    .roadAddress("도로명주소")
                    .area(100)
                    .type("공공시설")
                    .build();
            result.add(shelter);
        }
        when(shelterService.findAllByCurrent(lat, lon, ShelterVariable.DEFAULT_RADIUS))
                .thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/current")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(total));

    }

    @Test
    public void getSheltersByCurrent_반경_입력_테스트() throws Exception{
        //given
        int radius = 200;
        int total = 30;
        double lat = 37.123000;
        double lon = 127.123000;
        List<Shelter> result = new ArrayList<>();
        for (int i = total; i > 0; i--) {
            CivilDefenseShelter shelter = CivilDefenseShelter.builder()
                    .id((long) i)
                    .name("대피소" + i)
                    .address(new Address("서울시", "동대문구", "전농동", "전일중학교"))
                    .latitude(37.123456)
                    .longitude(127.123456)
                    .dong(null)
                    .roadAddress("도로명주소")
                    .area(100)
                    .type("공공시설")
                    .build();
            result.add(shelter);
        }
        when(shelterService.findAllByCurrent(lat, lon, radius))
                .thenReturn(result);

        ///when
        //then
        mockMvc.perform((get("/api/shelters/current")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))
                        .param("radius", String.valueOf(radius))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(total));
    }

}