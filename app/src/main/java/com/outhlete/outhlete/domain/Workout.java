package com.outhlete.outhlete.domain;

import java.util.List;

public class Workout {
    private final List<Exercise> exercises;

    public Workout(final List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public int getTotalDuration() {
        int total = 0;
        for (final Exercise exercise : exercises) {
            total += exercise.getDuration();
        }
        return total;
    }

    public List<Exercise> getExercises(){
        return this.exercises;
    }
}
