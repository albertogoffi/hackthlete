package com.outhlete.outhlete.algorithm;

import com.google.android.gms.maps.model.LatLng;
import com.outhlete.outhlete.domain.Exercise;
import com.outhlete.outhlete.domain.PubliBikeStation;
import com.outhlete.outhlete.domain.Workout;
import com.outhlete.outhlete.utils.By;
import com.outhlete.outhlete.utils.LocationUtils;

import java.util.List;

public class WorkoutCreator {
    private final List<Exercise> exercises;
    private final List<PubliBikeStation> stations;

    public WorkoutCreator(final List<Exercise> exercises, final List<PubliBikeStation> stations) {
        this.exercises = exercises;
        this.stations = stations;
    }

    // TODO implement
    public Workout makeWorkout(LatLng start, int duration) {
        // FIXME this can be inaccurate due to rounding.
        int warmupDuration = (int)(duration * 0.1);
        int muscleDuration = (int)(duration * 0.4);
        int cardioDuration = (int)(duration * 0.3);
        int stretchingDuration = (int)(duration * 0.1);
        int coolDownDuration = duration - warmupDuration - muscleDuration - cardioDuration - stretchingDuration;

        // Phase 1 --
        // ...
        for (final Exercise exercise : exercises) {
            getMinimumTravelTime(start, exercise.getStartPosition());
        }


        return null;
    }

    private double getMinimumTravelTime(LatLng start, LatLng end) {
        double travelTimeJogging = LocationUtils.getTravelTime(start, end, By.JOGGING);

        
    }

    private PubliBikeStation getNearestPubliBikeStation(LatLng position){
        double min = Double.MAX_VALUE;
        PubliBikeStation nearest = null;
        for(PubliBikeStation station : stations){
           double dist = LocationUtils.getTravelTime(position, station.getPosition(), By.JOGGING);
           if(dist < min){
               nearest = station;
               min = dist;
           }
        }
        return nearest;
    }
}
