package com.outhlete.outhlete.algorithm;

import com.google.android.gms.maps.model.LatLng;
import com.outhlete.outhlete.domain.Exercise;
import com.outhlete.outhlete.domain.PubliBikeStation;
import com.outhlete.outhlete.domain.Workout;
import com.outhlete.outhlete.utils.By;
import com.outhlete.outhlete.utils.LocationUtils;

import java.util.ArrayList;
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
        List<Exercise> workoutExercises = new ArrayList<>();

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

        return new Workout(workoutExercises);
    }

    private double getMinimumTravelTime(LatLng start, LatLng end) {
        double travelTimeJogging = LocationUtils.getTravelTime(start, end, By.JOGGING);
        double travelTimeUsingPubliBike = getTravelTimeUsingPubliBike(start, end);
        return Math.min(travelTimeJogging, travelTimeUsingPubliBike);
    }

    private double getTravelTimeUsingPubliBike(LatLng start, LatLng end) {
        PubliBikeStation nearestToStart = getNearestPubliBikeStation(start);
        PubliBikeStation nearestToEnd = getNearestPubliBikeStation(end);

        double travelTimeJoggingFromStart = LocationUtils.getTravelTime(start, nearestToStart.getPosition(), By.JOGGING);
        double travelTimeBiking = LocationUtils.getTravelTime(nearestToStart.getPosition(), nearestToEnd.getPosition(), By.BIKING);
        double travelTimeJoggingToEnd = LocationUtils.getTravelTime(start, nearestToEnd.getPosition(), By.JOGGING);
        return travelTimeJoggingFromStart + travelTimeBiking + travelTimeJoggingToEnd;
    }

    private PubliBikeStation getNearestPubliBikeStation(LatLng position) {
        double minTravelTime = Double.MAX_VALUE;
        PubliBikeStation nearestStation = null;
        for (final PubliBikeStation station : stations) {
           double travelTime = LocationUtils.getTravelTime(position, station.getPosition(), By.JOGGING);
           if (travelTime < minTravelTime){
               nearestStation = station;
               minTravelTime = travelTime;
           }
        }
        return nearestStation;
    }
}
