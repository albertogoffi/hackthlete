package com.outhlete.outhlete.algorithm;

import com.google.maps.android.SphericalUtil;
import com.outhlete.outhlete.domain.Exercise;
import com.outhlete.outhlete.domain.PubliBikeStation;
import com.outhlete.outhlete.domain.Workout;

import java.util.List;

public class WorkoutCreator {
    private final List<Exercise> exercises;
    private final List<PubliBikeStation> stations;

    public WorkoutCreator(final List<Exercise> exercises, final List<PubliBikeStation> stations) {
        this.exercises = exercises;
        this.stations = stations;
    }



    // TODO implement
    public Workout makeWorkout(int duration) {
        return null;
    }
}
