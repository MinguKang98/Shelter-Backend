package com.example.shelter.tsunamishelter.service;

import com.example.shelter.dong.Dong;
import com.example.shelter.exception.notfound.TsunamiShelterNotFoundException;
import com.example.shelter.sido.Sido;
import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.repository.TsunamiShelterRepository;
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
public class TsunamiShelterService {

    private final TsunamiShelterRepository tsunamiShelterRepository;

    public TsunamiShelter findById(Long id) {
        return tsunamiShelterRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new TsunamiShelterNotFoundException(id));
    }

    public List<TsunamiShelter> findAllByDong(Dong dong) {
        return tsunamiShelterRepository.findAllByDongNotDeleted(dong);
    }

    public Page<TsunamiShelter> findAllByDong(Dong dong, Pageable pageable) {
        return tsunamiShelterRepository.findAllByDongNotDeleted(dong, pageable);
    }

    public List<TsunamiShelter> findAllByCurrent(double curLat, double curLon, double radius) {

        SquareGpsRange squareGpsRange = GpsUtils.getSquareGpsRange(curLat, curLon, radius);

        List<TsunamiShelter> squareTsunamiShelters = tsunamiShelterRepository
                .findAllBySquareRangeNotDeleted(
                        squareGpsRange.getMinLat(),
                        squareGpsRange.getMaxLat(),
                        squareGpsRange.getMinLon(),
                        squareGpsRange.getMaxLon());

        return squareTsunamiShelters.stream().filter(
                        t -> GpsUtils.getDistance(t.getLatitude(), t.getLongitude(), curLat, curLon) < radius
                )
                .collect(Collectors.toList());
    }

    public int countAll() {
        return tsunamiShelterRepository.countAllNotDeleted();
    }

    public int countAllBySido(Sido sido) {
        return tsunamiShelterRepository.countAllBySidoNotDeleted(sido);
    }

}
