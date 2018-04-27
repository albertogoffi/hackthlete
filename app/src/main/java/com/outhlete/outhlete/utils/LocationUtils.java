package com.outhlete.outhlete.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class LocationUtils {
    public static double getDistance(LatLng start, LatLng end) {
        return SphericalUtil.computeDistanceBetween(start, end);
    }

    public static double getTravelTime(LatLng start, LatLng end, By by) {
        double distance = getDistance(start, end);
        return distance / by.getMetersPerMinute();
    }
}
