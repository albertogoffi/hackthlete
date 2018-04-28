package com.outhlete.outhlete.domain;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PubliBikeExercise extends Exercise {

    private final List<Exercise> segmentExercises;

    public PubliBikeExercise(LatLng startPosition, LatLng endPosition, int duration, Goal goal, List<Exercise> segmentExercises) {
        super("Move with a bike", "Move from start to end using PubliBike",
                startPosition, endPosition, duration, null , goal);
        this.segmentExercises = segmentExercises;
    }

    public List<Exercise> getSegmentExercises(){
        return this.segmentExercises;
    }
}