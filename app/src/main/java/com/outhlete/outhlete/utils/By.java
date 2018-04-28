package com.outhlete.outhlete.utils;

public enum By {
    // FIXME -- check whether these values are realistic.
    WALKING(100.0),
    JOGGING(120.0),
    RUNNING(160.0),
    BIKING(260.0);

    private final double metersPerMinute;

    By(double metersPerMinute) {
        this.metersPerMinute = metersPerMinute;
    }

    public double getMetersPerMinute() {
        return metersPerMinute;
    }
}
