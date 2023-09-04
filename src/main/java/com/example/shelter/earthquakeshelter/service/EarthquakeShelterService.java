package com.example.shelter.earthquakeshelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.earthquakeshelter.EarthquakeShelter;
import com.example.shelter.earthquakeshelter.repository.EarthquakeShelterRepository;
import com.example.shelter.exception.notfound.EarthquakeShelterNotFoundException;
import com.example.shelter.sido.Sido;
import com.example.shelter.util.GpsUtils;
import com.example.shelter.util.SquareGpsRange;
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

        SquareGpsRange squareGpsRange = GpsUtils.getSquareGpsRange(curLat, curLon, radius);

        List<EarthquakeShelter> squareEarthquakeShelters = earthquakeShelterRepository
                .findAllBySquareRangeNotDeleted(
                        squareGpsRange.getMinLat(),
                        squareGpsRange.getMaxLat(),
                        squareGpsRange.getMinLon(),
                        squareGpsRange.getMaxLon());

        return squareEarthquakeShelters.stream().filter(
                        t -> GpsUtils.getDistance(t.getLatitude(), t.getLongitude(), curLat, curLon) <= radius
                )
                .collect(Collectors.toList());
    }

    public int countAll() {
        return earthquakeShelterRepository.countAllNotDeleted();
    }


    public int countAllBySido(Sido sido) {
        return earthquakeShelterRepository.countAllBySidoNotDeleted(sido);
    }

}
