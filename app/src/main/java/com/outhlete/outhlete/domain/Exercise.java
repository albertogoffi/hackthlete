package com.outhlete.outhlete.domain;

import com.google.android.gms.maps.model.LatLng;
import com.outhlete.outhlete.utils.By;
import com.outhlete.outhlete.utils.LocationUtils;

public class Exercise {
    private final String name;
    private final String description;
    private final LatLng startPosition;
    private final LatLng endPosition;
    private final int duration;
    private final String image;
    private final Goal goal;

    public Exercise(String name, String description, LatLng startPosition, LatLng endPosition, int duration, String image, Goal goal) {
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

    public String getImage() {
        return image;
    }

    public Goal getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Exercise buildRunningExercise(LatLng start, LatLng end, Goal goal){

        double travelTimeJogging = 0;
        String name;
        String image;
        switch(goal){
            case WARM_UP:
            case COOL_DOWN:
                 travelTimeJogging = LocationUtils.getTravelTime(start, end, By.JOGGING);
                 name = "Jogging";
                 image = "jogging.jpg";
                 break;
            case CARDIO:
                travelTimeJogging = LocationUtils.getTravelTime(start, end, By.RUNNING);
                name = "Running";
                image = "cardio.jpg";
                break;
            default:
                throw new RuntimeException("error");
        }
        Exercise running = new Exercise(name, "Jog until point", start,  end, (int)travelTimeJogging, image, goal);
        return running;
    }

    public static Exercise buildBikingExercise(LatLng start, LatLng end, Goal goal){

        double travelTimeBiking = LocationUtils.getTravelTime(start, end, By.BIKING);
        Exercise jogging = new Exercise("Biking", "Ride Bike until point", start,  end, (int)travelTimeBiking, "bike.jpg", goal);
        return jogging;
    }
}
