package com.outhlete.outhlete.domain;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    private final List<Exercise> exercises;

    public Workout(final List<Exercise> exercises) {

        this.exercises = new ArrayList<>();
        for(Exercise exercise:exercises){
            if(exercise instanceof PubliBikeExercise){
                this.exercises.addAll(((PubliBikeExercise) exercise).getSegmentExercises());
            }else{
                this.exercises.add(exercise);
            }
        }
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
