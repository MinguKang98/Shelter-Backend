package com.example.shelter.util;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

public class TransCoord {

    public static ProjCoordinate transform(String strX, String strY) {

        double xCord = Double.parseDouble(strX);
        double yCord = Double.parseDouble(strY);

        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem epsg5174 = factory.createFromParameters("EPSG:5174","+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43");
        CoordinateReferenceSystem wgs84 = factory.createFromParameters("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
        BasicCoordinateTransform transformer = new BasicCoordinateTransform(epsg5174, wgs84);

        ProjCoordinate beforeCoord = new ProjCoordinate(xCord, yCord);
        ProjCoordinate afterCoord = new ProjCoordinate();

        return transformer.transform(beforeCoord, afterCoord);
    }

}
