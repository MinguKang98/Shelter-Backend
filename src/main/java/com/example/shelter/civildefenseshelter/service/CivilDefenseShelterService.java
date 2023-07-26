package com.example.shelter.civildefenseshelter.service;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.civildefenseshelter.repository.CivilDefenseShelterRepository;
import com.example.shelter.dong.Dong;
import com.example.shelter.exception.notfound.CivilDefenseShelterNotFoundException;
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
public class CivilDefenseShelterService {

    private final CivilDefenseShelterRepository civilDefenseShelterRepository;

    public CivilDefenseShelter findById(Long id) {
        return civilDefenseShelterRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new CivilDefenseShelterNotFoundException(id));
    }

    public Page<CivilDefenseShelter> findAllByDong(Dong dong, Pageable pageable) {
        return civilDefenseShelterRepository.findAllByDongNotDeleted(dong, pageable);
    }

    /**
     * 특정 GPS 좌표를 기준으로 반경 거리 내에 있는 CivilDefenseShelter 들을 가져온다.
     *
     * @param curLat GPS 좌표의 위도
     * @param curLon GPS 좌표의 경도
     * @param radius 반경 거리 (m)
     * @return 특정 GPS 좌표 기준 반경 거리 내에 있는 CivilDefenseShelter
     */
    public List<CivilDefenseShelter> findAllByCurrent(double curLat, double curLon, double radius) {

        SquareGpsRange squareGpsRange = GpsUtils.getSquareGpsRange(curLat, curLon, radius);

        List<CivilDefenseShelter> squareCivilDefenseShelters = civilDefenseShelterRepository
                .findAllBySquareRangeNotDeleted(
                        squareGpsRange.getMinLat(),
                        squareGpsRange.getMaxLat(),
                        squareGpsRange.getMinLon(),
                        squareGpsRange.getMaxLon());

        return squareCivilDefenseShelters.stream().filter(
                        t -> GpsUtils.getDistance(t.getLatitude(), t.getLongitude(), curLat, curLon) <= radius
                )
                .collect(Collectors.toList());
    }

    public int countAll() {
        return civilDefenseShelterRepository.countAllNotDeleted();
    }

    public int countAllBySido(Sido sido) {
        return civilDefenseShelterRepository.countAllBySidoNotDeleted(sido);
    }

}
