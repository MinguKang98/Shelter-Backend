package com.example.shelter.util;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

public class GpsUtils {

    private static final int EARTH_RADIUS = 6371;   // km

    /**
     * 두 GPS 좌표 (GPS1 과 GPS2) 사이의 거리를 반환한다. 이때 거리의 단위는 m
     *
     * @param lat1 GPS1 의 위도
     * @param lon1 GPS1 의 경도
     * @param lat2 GPS2 의 위도
     * @param lon2 GPS2 의 경도
     * @return 두 GPS 좌표 사이의 거리
     */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c * 1000;
    }


    /**
     * 현재 GPS 좌표 기준으로 반지름이 radius 인 원에 외접하는 정사각형을 이루는 위도와 경도들을 반환한다.
     * @param curLat 현재 위도
     * @param curLon 현재 경도
     * @param radius 원의 반지름
     * @return 원에 외접하는 정사각형을 이루는 위도와 경도의 양 끝 값이 담긴 SquareGpsRange
     */
    public static SquareGpsRange getSquareGpsRange(double curLat, double curLon, double radius) {
        double meterPerLat = (1 / (EARTH_RADIUS * Math.PI / 180)) / 1000;
        double meterPerLon = (1 / (EARTH_RADIUS * Math.PI / 180 * Math.cos(Math.toRadians(curLat)))) / 1000;

        double latRange = radius * meterPerLat;
        double lonRange = radius * meterPerLon;

        double minLat = Double.parseDouble(String.format("%.6f", curLat - latRange));
        double maxLat = Double.parseDouble(String.format("%.6f", curLat + latRange));
        double minLon = Double.parseDouble(String.format("%.6f", curLon - lonRange));
        double maxLon = Double.parseDouble(String.format("%.6f", curLon + lonRange));

        return new SquareGpsRange(minLat, maxLat, minLon, maxLon);
    }


    /**
     * EPSG:5174 좌표를 EPSG:4326 좌표로 변환한다.
     *
     * @param strX EPSG:5174 좌표계의 x좌표
     * @param strY EPSG:5174 좌표계의 y좌표
     * @return 변환된 EPSG:4326 좌표가 담긴 ProjCoordinate 객체
     */
    public static ProjCoordinate transform5174To4326(String strX, String strY) {

        double xCord = Double.parseDouble(strX);
        double yCord = Double.parseDouble(strY);

        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem epsg5174 = factory.createFromParameters("EPSG:5174", "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43");
        CoordinateReferenceSystem wgs84 = factory.createFromParameters("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
        BasicCoordinateTransform transformer = new BasicCoordinateTransform(epsg5174, wgs84);

        ProjCoordinate beforeCoord = new ProjCoordinate(xCord, yCord);
        ProjCoordinate afterCoord = new ProjCoordinate();

        return transformer.transform(beforeCoord, afterCoord);
    }

}
