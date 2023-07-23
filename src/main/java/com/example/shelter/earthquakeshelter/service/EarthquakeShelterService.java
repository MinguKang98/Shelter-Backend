package com.example.shelter.earthquakeshelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.repository.EarthquakeShelterRepository;
import com.example.shelter.exception.notfound.EarthquakeShelterNotFoundException;
import com.example.shelter.sido.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EarthquakeShelterService {

    private final EarthquakeShelterRepository earthquakeShelterRepository;
    private final int EARTH_RADIUS = 6371;   // km


    public EarthquakeShelter findById(Long id) {
        return earthquakeShelterRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new EarthquakeShelterNotFoundException(id));
    }


    public Page<EarthquakeShelter> findAllByDong(Dong dong, Pageable pageable) {
        return earthquakeShelterRepository.findAllByDongNotDeleted(dong, pageable);
    }


    /**
     * 특정 GPS 좌표를 기준으로 반경 거리 내에 있는 EarthquakeShelter 들을 가져온다.
     *
     * @param curLat GPS 좌표의 위도
     * @param curLon GPS 좌표의 경도
     * @param radius 반경 거리 (m)
     * @return 특정 GPS 좌표 기준 반경 거리 내에 있는 EarthquakeShelter
     */
    public List<EarthquakeShelter> findAllByCurrent(double curLat, double curLon, double radius) {

        double meterPerLat = (1 / (EARTH_RADIUS * Math.PI / 180)) / 1000;
        double meterPerLon = (1 / (EARTH_RADIUS * Math.PI / 180 * Math.cos(Math.toRadians(curLat)))) / 1000;

        double latRange = radius * meterPerLat;
        double lonRange = radius * meterPerLon;

        double minLat = Double.parseDouble(String.format("%.6f", curLat - latRange));
        double maxLat = Double.parseDouble(String.format("%.6f", curLat + latRange));
        double minLon = Double.parseDouble(String.format("%.6f", curLon - lonRange));
        double maxLon = Double.parseDouble(String.format("%.6f", curLon + lonRange));

        List<EarthquakeShelter> squareEarthquakeShelters = earthquakeShelterRepository
                .findAllBySquareRangeNotDeleted(minLat, maxLat, minLon, maxLon);

        return squareEarthquakeShelters.stream().filter(
                        t -> getDistance(t.getLatitude(), t.getLongitude(), curLat, curLon) <= radius
                )
                .collect(Collectors.toList());
    }


    /**
     * 두 GPS 좌표 (GPS1 과 GPS2) 사이의 거리를 반환한다. 이때 거리의 단위는 m
     *
     * @param lat1 GPS1 의 위도
     * @param lon1 GPS1 의 경도
     * @param lat2 GPS2 의 위도
     * @param lon2 GPS2 의 경도
     * @return 두 GPS 좌표 사이의 거리
     */
    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c * 1000;
        return d;
    }


    public int countAll() {
        return earthquakeShelterRepository.countAllNotDeleted();
    }


    public int countAllBySido(Sido sido) {
        return earthquakeShelterRepository.countAllBySidoNotDeleted(sido);
    }

}
