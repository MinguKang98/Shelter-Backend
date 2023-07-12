package com.example.shelter.tsunamishelter.service;

import com.example.shelter.tsunamishelter.TsunamiShelter;
import com.example.shelter.tsunamishelter.repository.TsunamiShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TsunamiShelterService {

    private final TsunamiShelterRepository tsunamiShelterRepository;
    private final int EARTH_RADIUS = 6371000;

    // findById

    // findAllByDong

    public List<TsunamiShelter> findAllByCurrent(double curLat, double curLon, double radius) {

        double meterPerLat = 1 / (EARTH_RADIUS * Math.PI / 180);
        double meterPerLon = 1 / (EARTH_RADIUS * Math.PI / 180 * Math.cos(Math.toRadians(curLon)));

        double latRange = radius * meterPerLat;
        double lonRange = radius * meterPerLon;

        double minLat = curLat - latRange;
        double maxLat = curLat + latRange;
        double minLon = curLon - lonRange;
        double maxLon = curLon + lonRange;

        List<TsunamiShelter> squareTsunamiShelters = tsunamiShelterRepository
                .findAllBySquareRangeNotDeleted(minLat, maxLat, minLon, maxLon);

        return squareTsunamiShelters.stream().filter(
                        t -> getDistance(t.getLatitude(), t.getLongitude(), curLat, curLon) < radius
                )
                .collect(Collectors.toList());
    }


    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c * 1000;    // Distance in m
        return d;
    }

}
