package com.outhlete.outhlete.domain;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Workout {
    private final List<Exercise> exercises;

    public Workout(final List<Exercise> exercises) {

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .queryRateLimit(3)
                .apiKey("AIzaSyBq6e5OnxObqIWurfzay99fkCZQDvsVjOU")
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build();


        this.exercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise instanceof PubliBikeExercise) {
                for(Exercise segmentExercise:((PubliBikeExercise) exercise).getSegmentExercises()){
                    this.exercises.add(addAddressToMovingExercises(geoApiContext, segmentExercise));
                }
            } else {
                this.exercises.add(addAddressToMovingExercises(geoApiContext, exercise));
            }
        }
    }

    private Exercise addAddressToMovingExercises(GeoApiContext geoApiContext, Exercise exercise){
        if(!exercise.getStartPosition().equals(exercise.getEndPosition())){
            try {
                com.google.maps.model.LatLng position = new com.google.maps.model.LatLng(exercise.getEndPosition().latitude, exercise.getEndPosition().longitude);
                GeocodingResult reversedPosition = GeocodingApi.reverseGeocode(geoApiContext, position).await()[0];
                String desc = exercise.getDescription()+reversedPosition.formattedAddress;
                return new Exercise(exercise.getName(), desc, exercise.getStartPosition(), exercise.getEndPosition(),
                        exercise.getDuration(), exercise.getImage(),exercise.getGoal());
            }catch(Exception e){}
        }else{
            return exercise;
        }
        return null;
    }

    public int getTotalDuration() {
        int total = 0;
        for (final Exercise exercise : exercises) {
            total += exercise.getDuration();
        }
        return total;
    }

    public List<Exercise> getExercises() {
        return this.exercises;
    }

    public List<LatLng> getWayPoints() {
        List<LatLng> wayPoints = new ArrayList<>();
        for (Exercise exercise : this.exercises) {
            if (wayPoints.isEmpty() || !wayPoints.get(wayPoints.size() - 1).equals(exercise.getStartPosition())) {
                wayPoints.add(exercise.getStartPosition());
            }
            if (!wayPoints.get(wayPoints.size() - 1).equals(exercise.getEndPosition())) {
                wayPoints.add(exercise.getEndPosition());
            }
        }
        return wayPoints;
    }
}
