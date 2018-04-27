package com.outhlete.outhlete.domain;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;


public class Exercise {
    private final String name;
    private final String description;
    private final LatLng startPosition;
    private final LatLng endPosition;
    private final int duration;
    private final URL image;
    private final Goal goal;

    public Exercise(String name, String description, LatLng startPosition, LatLng endPosition, int duration, URL image, Goal goal) {
        this.name = name;
        this.description = description;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.duration = duration;
        this.image = image;
        this.goal = goal;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getStartPosition() {
        return startPosition;
    }

    public LatLng getEndPosition() {
        return endPosition;
    }

    public int getDuration() {
        return duration;
    }

    public URL getImage() {
        return image;
    }

    public Goal getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return name;
    }
}
