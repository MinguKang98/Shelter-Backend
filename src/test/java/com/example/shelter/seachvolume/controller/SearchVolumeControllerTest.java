package com.example.shelter.seachvolume.controller;

import com.example.shelter.dong.Dong;
import com.example.shelter.dong.DongService;
import com.example.shelter.exception.notfound.DongNotFoundException;
import com.example.shelter.exception.notfound.SidoNotFoundException;
import com.example.shelter.seachvolume.dto.ShelterTypeVolumeDto;
import com.example.shelter.seachvolume.service.SearchVolumeService;
import com.example.shelter.sido.Sido;
import com.example.shelter.sido.SidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchVolumeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SearchVolumeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchVolumeService searchVolumeService;

    @MockBean
    SidoService sidoService;

    @MockBean
    DongService dongService;

    @Test
    public void increaseSearchVolumes_성공_테스트() throws Exception {
        //given
        Long dongId = 1L;
        String type = "tsunami";

        ///when
        //then
        mockMvc.perform(put("/api/search-volumes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(increaseInfoJson
                                .replace("$dongId", dongId.toString())
                                .replace("$type", type)
                        ))
                .andExpect(status().isNoContent());
    }

    @Test
    public void increaseSearchVolumes_유효성_실패_테스트1() throws Exception {
        //given
        Long dongId = 0L;
        String type = "tsunami";

        ///when
        //then
        mockMvc.perform(put("/api/search-volumes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(increaseInfoJson
                                .replace("$dongId", dongId.toString())
                                .replace("$type", type)
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(000));
    }

    @Test
    public void increaseSearchVolumes_유효성_실패_테스트2() throws Exception {
        //given
        Long dongId = 0L;
        String type = "fsdafsd";

        ///when
        //then
        mockMvc.perform(put("/api/search-volumes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(increaseInfoJson
                                .replace("$dongId", dongId.toString())
                                .replace("$type", type)
                        ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(107));
    }

    @Test
    public void getTotalSearchVolume_테스트() throws Exception {
        //given
        LocalDate now = LocalDate.now();
        int count = 10;
        when(searchVolumeService.getTotalVolumeByDate(now)).thenReturn(count);

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(count));
    }

    @Test
    public void getWeekTotalSearchVolume_테스트() throws Exception {
        //given
        LocalDate now = LocalDate.now();
        int count = 10;
        when(searchVolumeService.getTotalVolumeByDateRange(now.minusDays(7), now))
                .thenReturn(count);

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/week-total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(count));
    }

    @Test
    public void getSidoVolumes_테스트() throws Exception {
        //given
        LocalDate now = LocalDate.now();
        ShelterTypeVolumeDto dto = getShelterTypeVolumeDto1();
        when(searchVolumeService.getSidoVolumeMap(now)).thenReturn(dto);

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/sidos"))
                .andExpect(status().isOk())
                .andExpect(content().string(shelterTypeVolumeJsonResult1));
    }

    @Test
    public void getSigunguVolumes_테스트() throws Exception {
        //given
        Long id = 1L;
        LocalDate now = LocalDate.now();
        Sido sido = new Sido(id, "서울특별시");
        ShelterTypeVolumeDto dto = getShelterTypeVolumeDto1();
        when(sidoService.findById(id)).thenReturn(sido);
        when(searchVolumeService.getSigunguVolumeMap(sido, now)).thenReturn(dto);

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/sidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(shelterTypeVolumeJsonResult1));
    }

    @Test
    public void getSigunguVolumes_존재하지_않는_시도_테스트() throws Exception {
        //given
        Long id = 1L;
        LocalDate now = LocalDate.now();
        Sido sido = new Sido(id, "서울특별시");
        when(sidoService.findById(id)).thenThrow(new SidoNotFoundException(id));

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/sidos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(100));
    }

    @Test
    public void getDateVolumes_테스트() throws Exception {
        //given
        Long id = 1L;
        LocalDate now = LocalDate.now();
        Dong dong = new Dong(id, "전농동", null);
        ShelterTypeVolumeDto dto = getShelterTypeVolumeDto2();
        when(dongService.findById(id)).thenReturn(dong);
        when(searchVolumeService.getDateVolumeMap(dong, now.minusDays(7), now))
                .thenReturn(dto);

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/week/dongs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(shelterTypeVolumeJsonResult2));
    }

    @Test
    public void getDateVolumes_존재하지_않는_동_테스트() throws Exception {
        //given
        Long id = 1L;
        LocalDate now = LocalDate.now();
        Dong dong = new Dong(id, "전농동", null);
        when(dongService.findById(id)).thenThrow(new DongNotFoundException(id));

        ///when
        //then
        mockMvc.perform(get("/api/search-volumes/week/dongs/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(102));
    }


    public ShelterTypeVolumeDto getShelterTypeVolumeDto1() {
        ShelterTypeVolumeDto dto = new ShelterTypeVolumeDto();
        dto.getTsunami().put("A", 0L);
        dto.getTsunami().put("B", 2L);
        dto.getEarthquake().put("A", 4L);
        dto.getEarthquake().put("B", 2L);
        dto.getCivilDefense().put("A", 0L);
        dto.getCivilDefense().put("B", 10L);
        return dto;
    }

    public ShelterTypeVolumeDto getShelterTypeVolumeDto2() {
        ShelterTypeVolumeDto dto = new ShelterTypeVolumeDto();
        LocalDate now = LocalDate.now();
        now.minusDays(7).datesUntil(now.plusDays(1))
                .map(d -> d.format(ShelterTypeVolumeDto.formatter))
                .forEach(s -> {
                    dto.getTsunami().put(s, 1L);
                    dto.getEarthquake().put(s, 2L);
                    dto.getCivilDefense().put(s, 3L);
                });

        return dto;
    }

    String increaseInfoJson = """
            {
                "dongId" : $dongId,
                "type" : "$type"
            }
            """;

    String shelterTypeVolumeJsonResult1 = "{\"tsunami\":{\"A\":0,\"B\":2},\"earthquake\":{\"A\":4,\"B\":2},\"civilDefense\":{\"A\":0,\"B\":10}}";

    String shelterTypeVolumeJsonResult2 = """
            {"tsunami":{"$d1":1,"$d2":1,"$d3":1,"$d4":1,"$d5":1,"$d6":1,"$d7":1,"$d8":1},"earthquake":{"$d1":2,"$d2":2,"$d3":2,"$d4":2,"$d5":2,"$d6":2,"$d7":2,"$d8":2},"civilDefense":{"$d1":3,"$d2":3,"$d3":3,"$d4":3,"$d5":3,"$d6":3,"$d7":3,"$d8":3}}"""
            .replace("$d1", LocalDate.now().minusDays(7).format(ShelterTypeVolumeDto.formatter))
            .replace("$d2", LocalDate.now().minusDays(6).format(ShelterTypeVolumeDto.formatter))
            .replace("$d3", LocalDate.now().minusDays(5).format(ShelterTypeVolumeDto.formatter))
            .replace("$d4", LocalDate.now().minusDays(4).format(ShelterTypeVolumeDto.formatter))
            .replace("$d5", LocalDate.now().minusDays(3).format(ShelterTypeVolumeDto.formatter))
            .replace("$d6", LocalDate.now().minusDays(2).format(ShelterTypeVolumeDto.formatter))
            .replace("$d7", LocalDate.now().minusDays(1).format(ShelterTypeVolumeDto.formatter))
            .replace("$d8", LocalDate.now().format(ShelterTypeVolumeDto.formatter));
}