package com.outhlete.outhlete;

import android.app.Application;

import com.outhlete.outhlete.domain.Workout;

public class App extends Application {
    private Workout workout;

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
}
