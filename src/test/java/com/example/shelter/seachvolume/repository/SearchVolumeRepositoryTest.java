package com.example.shelter.seachvolume.repository;

import com.example.shelter.dong.Dong;
import com.example.shelter.seachvolume.SearchVolume;
import com.example.shelter.seachvolume.dto.RegionVolumeDto;
import com.example.shelter.shelter.ShelterType;
import com.example.shelter.sido.Sido;
import com.example.shelter.sigungu.Sigungu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SearchVolumeRepositoryTest {

    @Autowired
    SearchVolumeRepository searchVolumeRepository;

    @Autowired
    TestEntityManager em;


    @Test
    public void findByDongAndTypeAndDateNotDeleted_존재하는_검색량_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        SearchVolume searchVolume = SearchVolume.builder()
                .volume(1)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        em.persistAndFlush(searchVolume);
        em.clear();

        ///when
        Optional<SearchVolume> findSearchVolume = searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now());

        //then
        assertThat(findSearchVolume).isNotEmpty();
        assertThat(findSearchVolume.get().getCreatedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void findByDongAndTypeAndDateNotDeleted_다른_동_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong1 = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong1);

        Dong dong2 = Dong.builder()
                .name("답십리동")
                .sigungu(sigungu)
                .build();
        em.persist(dong2);

        SearchVolume searchVolume = SearchVolume.builder()
                .volume(1)
                .dong(dong1)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        em.persistAndFlush(searchVolume);
        em.clear();

        ///when
        Optional<SearchVolume> findSearchVolume = searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong2, ShelterType.TSUNAMI, LocalDate.now());

        //then
        assertThat(findSearchVolume).isEmpty();
    }

    @Test
    public void findByDongAndTypeAndDateNotDeleted_다른_대피소종류_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        SearchVolume searchVolume = SearchVolume.builder()
                .volume(1)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        em.persistAndFlush(searchVolume);
        em.clear();

        ///when
        Optional<SearchVolume> findSearchVolume = searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.CIVIL_DEFENCE, LocalDate.now());

        //then
        assertThat(findSearchVolume).isEmpty();
    }

    @Test
    public void findByDongAndTypeAndDateNotDeleted_다른_날짜_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        SearchVolume searchVolume = SearchVolume.builder()
                .volume(1)
                .dong(dong)
                .shelterType(ShelterType.TSUNAMI)
                .build();
        em.persistAndFlush(searchVolume);
        em.clear();

        ///when
        Optional<SearchVolume> findSearchVolume = searchVolumeRepository
                .findByDongAndTypeAndDateNotDeleted(dong, ShelterType.TSUNAMI, LocalDate.now().minusDays(1));

        //then
        assertThat(findSearchVolume).isEmpty();
    }

    @Test
    public void getTotalVolumeByDateNotDeleted_0아닌_결과_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            SearchVolume searchVolume = SearchVolume.builder()
                    .volume(i)
                    .dong(dong)
                    .shelterType(switch (i % 3) {
                        case 0 -> ShelterType.TSUNAMI;
                        case 1 -> ShelterType.EARTHQUAKE;
                        case 2 -> ShelterType.CIVIL_DEFENCE;
                        default -> null;
                    })
                    .build();
            em.persist(searchVolume);
        }
        em.flush();
        em.clear();

        ///when
        int totalVolume = searchVolumeRepository
                .getTotalVolumeByDateNotDeleted(LocalDate.now());

        //then
        assertThat(totalVolume).isEqualTo(45);
    }

    @Test
    public void getTotalVolumeByDateNotDeleted_다른_날짜_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 10; i++) {
            SearchVolume searchVolume = SearchVolume.builder()
                    .volume(i)
                    .dong(dong)
                    .shelterType(switch (i % 3) {
                        case 0 -> ShelterType.TSUNAMI;
                        case 1 -> ShelterType.EARTHQUAKE;
                        case 2 -> ShelterType.CIVIL_DEFENCE;
                        default -> null;
                    })
                    .build();
            em.persist(searchVolume);
        }
        em.flush();
        em.clear();

        ///when
        int totalVolume = searchVolumeRepository
                .getTotalVolumeByDateNotDeleted(LocalDate.now().minusDays(1));

        //then
        assertThat(totalVolume).isZero();
    }

    @Test
    public void getTotalVolumeByDateRangeNotDeleted_0아닌_결과_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                SearchVolume searchVolume = SearchVolume.builder()
                        .createdDate(LocalDate.now().minusDays(i))
                        .volume(10)
                        .dong(dong)
                        .shelterType(switch (j % 3) {
                            case 0 -> ShelterType.TSUNAMI;
                            case 1 -> ShelterType.EARTHQUAKE;
                            case 2 -> ShelterType.CIVIL_DEFENCE;
                            default -> null;
                        })
                        .build();
                em.persist(searchVolume);
            }
        }
        em.flush();
        em.clear();

        ///when
        int totalVolume = searchVolumeRepository
                .getTotalVolumeByDateRangeNotDeleted(LocalDate.now().minusDays(6), LocalDate.now());

        //then
        assertThat(totalVolume).isEqualTo(210);
    }

    @Test
    public void getTotalVolumeByDateRangeNotDeleted_다른_날짜범위_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                SearchVolume searchVolume = SearchVolume.builder()
                        .createdDate(LocalDate.now().minusDays(i))
                        .volume(j)
                        .dong(dong)
                        .shelterType(switch (j % 3) {
                            case 0 -> ShelterType.TSUNAMI;
                            case 1 -> ShelterType.EARTHQUAKE;
                            case 2 -> ShelterType.CIVIL_DEFENCE;
                            default -> null;
                        })
                        .build();
                em.persist(searchVolume);
            }
        }
        em.flush();
        em.clear();

        ///when
        int totalVolume = searchVolumeRepository.getTotalVolumeByDateRangeNotDeleted(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(6)
        );

        //then
        assertThat(totalVolume).isZero();
    }

    @Test
    public void findAllByDongAndDateRangeNotDeleted_존재하는_검색량_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 7; i++) {
            SearchVolume searchVolume1 = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(i + 10)
                    .dong(dong)
                    .shelterType(ShelterType.TSUNAMI)
                    .build();
            em.persist(searchVolume1);

            SearchVolume searchVolume2 = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(i + 10)
                    .dong(dong)
                    .shelterType(ShelterType.EARTHQUAKE)
                    .build();
            em.persist(searchVolume2);

            SearchVolume searchVolume3 = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(i + 10)
                    .dong(dong)
                    .shelterType(ShelterType.CIVIL_DEFENCE)
                    .build();
            em.persist(searchVolume3);
        }
        em.flush();
        em.clear();

        ///when
        List<SearchVolume> volumes = searchVolumeRepository
                .findAllByDongAndDateRangeNotDeleted(
                        dong,
                        LocalDate.now().minusDays(6),
                        LocalDate.now()
                );

        //then
        assertThat(volumes.size()).isEqualTo(21);
    }

    @Test
    public void findAllByDongAndDateRangeNotDeleted_잘못된_동_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong1 = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong1);

        Dong dong2 = Dong.builder()
                .name("답십리동")
                .sigungu(sigungu)
                .build();
        em.persist(dong2);

        for (int i = 0; i < 7; i++) {
            SearchVolume searchVolume = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(i + 10)
                    .dong(dong1)
                    .shelterType(ShelterType.TSUNAMI)
                    .build();
            em.persist(searchVolume);
        }
        em.flush();
        em.clear();

        ///when
        List<SearchVolume> volumes = searchVolumeRepository
                .findAllByDongAndDateRangeNotDeleted(
                        dong2,
                        LocalDate.now().minusDays(6),
                        LocalDate.now()
                );

        //then
        assertThat(volumes.size()).isZero();
    }


    @Test
    public void findAllByDongAndDateRangeNotDeleted_잠롯된_날짜범위_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        Sigungu sigungu = Sigungu.builder()
                .name("동대문구")
                .sido(sido)
                .build();
        em.persist(sigungu);

        Dong dong = Dong.builder()
                .name("전농동")
                .sigungu(sigungu)
                .build();
        em.persist(dong);

        for (int i = 0; i < 7; i++) {
            SearchVolume searchVolume = SearchVolume.builder()
                    .createdDate(LocalDate.now().minusDays(i))
                    .volume(i + 10)
                    .dong(dong)
                    .shelterType(ShelterType.TSUNAMI)
                    .build();
            em.persist(searchVolume);
        }
        em.flush();
        em.clear();

        ///when
        List<SearchVolume> volumes = searchVolumeRepository
                .findAllByDongAndDateRangeNotDeleted(
                        dong,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(6)
                );

        //then
        assertThat(volumes.size()).isZero();
    }

    @Test
    public void countSidoSearchVolumesByDateNotDeleted_테스트() {
        //given
        for (int i = 0; i < 10; i++) {
            Sido sido = Sido.builder()
                    .name("시도" + i)
                    .build();
            em.persist(sido);
            for (int j = 0; j < 5; j++) {
                Sigungu sigungu = Sigungu.builder()
                        .name("시군구" + (5 * i + j))
                        .sido(sido)
                        .build();
                em.persist(sigungu);

                Dong dong = Dong.builder()
                        .name("동" + (5 * i + j))
                        .sigungu(sigungu)
                        .build();
                em.persist(dong);

                SearchVolume searchVolume = SearchVolume.builder()
                        .volume(j)
                        .dong(dong)
                        .shelterType(switch (j % 3) {
                            case 0 -> ShelterType.TSUNAMI;
                            case 1 -> ShelterType.EARTHQUAKE;
                            case 2 -> ShelterType.CIVIL_DEFENCE;
                            default -> null;
                        })
                        .build();
                em.persist(searchVolume);
            }
        }
        em.flush();
        em.clear();

        ///when
        List<RegionVolumeDto> result = searchVolumeRepository
                .countSidoByDateNotDeleted(LocalDate.now());

        //then
        assertThat(result.size()).isEqualTo(30);
    }

    @Test
    public void countSigunguBySidoAndDateNotDeleted_테스트() {
        //given
        Sido sido = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido);

        for (int i = 0; i < 10; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("시군구" + i)
                    .sido(sido)
                    .build();
            em.persist(sigungu);

            for (int j = 0; j < 5; j++) {
                Dong dong = Dong.builder()
                        .name("동" + (5 * i + j))
                        .sigungu(sigungu)
                        .build();
                em.persist(dong);

                SearchVolume searchVolume = SearchVolume.builder()
                        .volume(j)
                        .dong(dong)
                        .shelterType(switch (j % 3) {
                            case 0 -> ShelterType.TSUNAMI;
                            case 1 -> ShelterType.EARTHQUAKE;
                            case 2 -> ShelterType.CIVIL_DEFENCE;
                            default -> null;
                        })
                        .build();
                em.persist(searchVolume);
            }
        }
        em.flush();
        em.clear();

        ///when
        List<RegionVolumeDto> result = searchVolumeRepository
                .countSigunguBySidoAndDateNotDeleted(sido, LocalDate.now());

        //then
        assertThat(result.size()).isEqualTo(30);
    }

    @Test
    public void countSigunguBySidoAndDateNotDeleted_잘못된_시도_테스트() {
        //given
        Sido sido1 = Sido.builder()
                .name("서울시")
                .build();
        em.persist(sido1);

        Sido sido2 = Sido.builder()
                .name("걍기도")
                .build();
        em.persist(sido2);

        for (int i = 0; i < 10; i++) {
            Sigungu sigungu = Sigungu.builder()
                    .name("시군구" + i)
                    .sido(sido1)
                    .build();
            em.persist(sigungu);

            for (int j = 0; j < 5; j++) {
                Dong dong = Dong.builder()
                        .name("동" + (5 * i + j))
                        .sigungu(sigungu)
                        .build();
                em.persist(dong);

                SearchVolume searchVolume = SearchVolume.builder()
                        .volume(j)
                        .dong(dong)
                        .shelterType(switch (j % 3) {
                            case 0 -> ShelterType.TSUNAMI;
                            case 1 -> ShelterType.EARTHQUAKE;
                            case 2 -> ShelterType.CIVIL_DEFENCE;
                            default -> null;
                        })
                        .build();
                em.persist(searchVolume);
            }
        }
        em.flush();
        em.clear();

        ///when
        List<RegionVolumeDto> result = searchVolumeRepository
                .countSigunguBySidoAndDateNotDeleted(sido2, LocalDate.now());

        //then
        assertThat(result.size()).isZero();
    }

}