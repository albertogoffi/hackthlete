package com.outhlete.outhlete.domain;

import com.google.android.gms.maps.model.LatLng;

public class PubliBikeStation {
    private final LatLng position;

    public PubliBikeStation(LatLng position) {
        this.position = position;
    }

    public LatLng getPosition() {
        return position;
    }
}
