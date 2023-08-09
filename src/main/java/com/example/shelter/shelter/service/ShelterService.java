package com.example.shelter.shelter.service;

import com.example.shelter.civildefenseshelter.CivilDefenseShelter;
import com.example.shelter.shelter.Shelter;
import com.example.shelter.shelter.repository.ShelterRepository;
import com.example.shelter.util.GpsUtils;
import com.example.shelter.util.SquareGpsRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;

    public List<Shelter> findAllByCurrent(double curLat, double curLon, double radius) {

        SquareGpsRange squareGpsRange = GpsUtils.getSquareGpsRange(curLat, curLon, radius);

        List<Shelter> squareCivilDefenseShelters = shelterRepository
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

}
