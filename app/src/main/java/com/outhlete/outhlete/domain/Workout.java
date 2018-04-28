package com.outhlete.outhlete.domain;

import com.google.android.gms.maps.model.LatLng;

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

    public List<LatLng> getWayPoints(){
        List<LatLng> wayPoints = new ArrayList<>();
        for(Exercise exercise:this.exercises){
            if(wayPoints.isEmpty() || !wayPoints.get(wayPoints.size()-1).equals(exercise.getStartPosition())){
                wayPoints.add(exercise.getStartPosition());
            }
            if(!wayPoints.get(wayPoints.size()-1).equals(exercise.getEndPosition())){
                wayPoints.add(exercise.getEndPosition());
            }
        }
        return wayPoints;
    }
}
