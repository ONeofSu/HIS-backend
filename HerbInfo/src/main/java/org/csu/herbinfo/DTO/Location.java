package org.csu.herbinfo.DTO;

import lombok.Data;

@Data
public class Location {
    private double longitude;   //经度
    private double latitude;    //维度

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static boolean validLocation(double longitude, double latitude) {
        if(longitude > 180 || longitude < -180) {
            return false;
        }
        if(latitude > 90 || latitude < -90) {
            return false;
        }
        return true;
    }
}
