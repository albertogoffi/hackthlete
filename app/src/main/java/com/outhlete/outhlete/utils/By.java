package com.outhlete.outhlete.utils;

enum By {
    // FIXME -- check whether these values are realistic.
    WALKING(100.0),
    JOGGING(200.0),
    RUNNING(300.0),
    BIKING(400.0);

    private final double metersPerMinute;

    By(double metersPerMinute) {
        this.metersPerMinute = metersPerMinute;
    }

    public double getMetersPerMinute() {
        return metersPerMinute;
    }
}
