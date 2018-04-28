package com.outhlete.outhlete.algorithm;

import com.google.android.gms.maps.model.LatLng;
import com.outhlete.outhlete.domain.Exercise;
import com.outhlete.outhlete.domain.Goal;
import com.outhlete.outhlete.domain.PubliBikeExercise;
import com.outhlete.outhlete.domain.PubliBikeStation;
import com.outhlete.outhlete.domain.Workout;
import com.outhlete.outhlete.utils.By;
import com.outhlete.outhlete.utils.FileUtils;
import com.outhlete.outhlete.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
WORKOUT CONSTRAINTS:
5 phases: warmup (10%), streatching (10%), muscles(40%), cardio(30%), cooldown(10%)
every phase has a accepted time range of +-15%
every muscle must be trained at least once (legs count as trained once by default).
If there is more time i can train some muscles twice starting from the big ones (legs, chest, back)
every streatching exercise last 1.5 minute
every muscle exercise lasts 5 minutes
 */
public class WorkoutCreator {
    private final List<Exercise> exercises;
    private final List<PubliBikeStation> stations;

    public WorkoutCreator(final List<Exercise> exercises, final List<PubliBikeStation> stations) {
        this.exercises = FileUtils.loadExercisesFromCSV();
        this.stations = FileUtils.loadPubliBikeStationsFromCSV();
    }

    public Workout makeWorkout(LatLng start, int duration) {
        List<Exercise> workoutExercises = new ArrayList<>();
        int scaledDuration = (int)(duration - (duration * 0.2));
        // FIXME this can be inaccurate due to rounding.
        int warmupDuration = (int)(scaledDuration * 0.1);
        int muscleDuration = (int)(scaledDuration * 0.4);
        int cardioDuration = (int)(scaledDuration * 0.3);
        int stretchingDuration = (int)(scaledDuration * 0.1);
        int coolDownDuration = scaledDuration - warmupDuration - muscleDuration - cardioDuration - stretchingDuration;

        //on the base of the time we have we calculate the goals to cover
        Goal[] stretchingGoals = {Goal.STRETCHING_LOW_BODY, Goal.STRETCHING_CORE_BODY, Goal.STRETCHING_TOP_BODY};
        int numberOfStretchingExercises = (int)(stretchingDuration/(1.5));
        List<Goal> stretchingGoalsToCover = new ArrayList<>();
        int i = 0;
        while(i < stretchingGoals.length && stretchingGoalsToCover.size() < numberOfStretchingExercises){
            stretchingGoalsToCover.add(stretchingGoals[i]);
            i = i+1 < stretchingGoals.length? ++i : 0;
        }

        Goal[] muscleGoals = {Goal.CHEST, Goal.BACK, Goal.ABS, Goal.ARMS, Goal.LEGS};
        int numberOfMuscleExercises = (int)(muscleDuration/(5));
        List<Goal> muscleGoalsToCover = new ArrayList<>();
        i = 0;
        while(i < muscleGoals.length && muscleGoalsToCover.size() < numberOfMuscleExercises){
            muscleGoalsToCover.add(muscleGoals[i]);
            i = i+1 < muscleGoals.length? ++i : 0;
        }

        List<Exercise> possibleWarmups = this.getPossibleJoggingExercises(start,
                this.getPositions(this.exercises), warmupDuration, Goal.WARM_UP);

        //warmup loop
        warmup: for(Exercise selectedWarmup:possibleWarmups) {
            //stretching
            List<Exercise> stretchingExercises = getExercisesThatFitGoals(selectedWarmup.getEndPosition(), stretchingGoalsToCover);
            for (Exercise stretchingExercise : stretchingExercises) {
                stretchingGoalsToCover.remove(stretchingExercise.getGoal());
            }
            if (stretchingGoalsToCover.size() > 0) {
                throw new RuntimeException("it should not happen");
            }

            //muscle
            List<Goal> muscleGoalsToCoverCopy = new ArrayList<>(muscleGoalsToCover);
            List<Exercise> muscleExercises = getExercisesThatFitGoals(selectedWarmup.getEndPosition(), muscleGoalsToCoverCopy);
            if (muscleExercises.size() == 0){
                continue warmup;
            }

            for(Exercise muscleExercise: muscleExercises){
                muscleGoalsToCoverCopy.remove(muscleExercise.getGoal());
            }

            Set<LatLng> possibleTargetPositionsCardio = new HashSet<>();
            for(Exercise targetExercise: this.exercises){
                if(!targetExercise.getStartPosition().equals(selectedWarmup.getEndPosition())){
                    possibleTargetPositionsCardio.add(targetExercise.getStartPosition());
                }
            }

            if(muscleGoalsToCoverCopy.size() == 0){
                //we add the publibike positions to have more options
                for(PubliBikeStation publiBikeStation: this.stations){
                    if(!publiBikeStation.getPosition().equals(selectedWarmup.getEndPosition())){
                        possibleTargetPositionsCardio.add(publiBikeStation.getPosition());
                    }
                }
            }

            List<Exercise> possibleCardioExercises = this.getPossibleCardioExercises(selectedWarmup.getEndPosition(),
                    possibleTargetPositionsCardio, cardioDuration);
            if(possibleCardioExercises.size() == 0){
                continue warmup;
            }

            cardio: for(Exercise selectedCardio:possibleCardioExercises) {
                List<Exercise> newMuscleExercise = new ArrayList<>();
                if(muscleGoalsToCoverCopy.size() > 0) {
                    newMuscleExercise = getExercisesThatFitGoals(selectedCardio.getEndPosition(),
                            muscleGoalsToCoverCopy);
                    for(Exercise muscleExercise: newMuscleExercise){
                        muscleGoalsToCoverCopy.remove(muscleExercise.getGoal());
                    }
                    if(muscleGoalsToCoverCopy.size() > 0){
                        continue cardio;
                    }
                }
                Set<LatLng> targetPos = new HashSet<>();
                targetPos.add(start);
                List<Exercise> possibleCooldowns = this.getPossibleJoggingExercises(selectedCardio.getEndPosition(),
                        targetPos, coolDownDuration, Goal.COOL_DOWN);
                if(possibleCooldowns.size() == 0){
                    continue cardio;
                }
                workoutExercises.add(selectedWarmup);
                workoutExercises.addAll(stretchingExercises);
                workoutExercises.add(selectedCardio);
                workoutExercises.addAll(newMuscleExercise);
                workoutExercises.add(possibleCooldowns.get(0));
                return new Workout(workoutExercises);
            }
        }

        return new Workout(workoutExercises);
    }

    private PubliBikeExercise getTravelUsingPubliBike(LatLng start, LatLng end, Goal goal) {
        PubliBikeStation nearestToStart = getNearestPubliBikeStation(start);
        PubliBikeStation nearestToEnd = getNearestPubliBikeStation(end);

        if(nearestToStart == nearestToEnd){
            return null;
        }

        List<Exercise> exercises = new ArrayList<>();
        Exercise firstSegment = Exercise.buildRunningExercise(start, nearestToStart.getPosition(), goal);
        if(firstSegment.getDuration() > 0) {
            exercises.add(firstSegment);
        }
        exercises.add(Exercise.buildBikingExercise(nearestToStart.getPosition(), nearestToEnd.getPosition(), goal));
        Exercise lastSegment = Exercise.buildRunningExercise(nearestToEnd.getPosition(), end, goal);
        if(lastSegment.getDuration() > 0) {
            exercises.add(lastSegment);
        }
        int totalDuration = 0;
        for(Exercise exercise: exercises){
            totalDuration += exercise.getDuration();
        }

        return new PubliBikeExercise(start, end, totalDuration, goal, exercises);
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

    private List<Exercise> getPossibleJoggingExercises(final LatLng startPosition, Set<LatLng> targetPositions, final int duration, Goal goal){

        int durationMax = (int)(duration + (duration * 0.15));
        int durationMin = (int)(duration - (duration * 0.15));

        List<Exercise> joggingExercises = new ArrayList<>();
        for(LatLng position:targetPositions){
            Exercise jogging = Exercise.buildRunningExercise(startPosition, position, goal);
            if(jogging.getDuration() <= durationMax && jogging.getDuration() >= durationMin){
                joggingExercises.add(jogging);
            }

            Exercise biking = this.getTravelUsingPubliBike(startPosition, position, goal);
            if(biking!= null && biking.getDuration() <= durationMax && biking.getDuration() >= durationMin){
                joggingExercises.add(biking);
            }
        }
        Collections.sort(joggingExercises, new Comparator<Exercise>(){
            public int compare(Exercise a, Exercise b){
                int diffA = Math.abs(a.getDuration()-duration);
                int diffB = Math.abs(b.getDuration()-duration);
                return Integer.compare(diffA, diffB);
            }
        });

        return joggingExercises;
    }

    private List<Exercise> getPossibleCardioExercises(final LatLng startPosition, Set<LatLng> targetPositions, final int duration){

        int durationMax = (int)(duration + (duration * 0.15));
        int durationMin = (int)(duration - (duration * 0.15));

        List<Exercise> cardioExercises = new ArrayList<>();
        for(LatLng possibleExercisePosition:targetPositions){
            Exercise cardio = Exercise.buildRunningExercise(startPosition, possibleExercisePosition, Goal.CARDIO);
            if(cardio.getDuration() <= durationMax && cardio.getDuration() >= durationMin){
                cardioExercises.add(cardio);
            }
        }

        Collections.sort(cardioExercises, new Comparator<Exercise>(){
            public int compare(Exercise a, Exercise b){
                int diffA = Math.abs(a.getDuration()-duration);
                int diffB = Math.abs(b.getDuration()-duration);
                return Integer.compare(diffA, diffB);
            }
        });

        return cardioExercises;
    }

    private List<Exercise> getExercisesThatFitGoals(final LatLng startPosition, List<Goal> goalsToCover){

        List<Exercise> availableExercisesHere = new ArrayList<>();
        //some randomness
        Collections.shuffle(exercises);

        for(Exercise exercise:exercises){
            if(exercise.getStartPosition().equals(startPosition) && goalsToCover.contains(exercise.getGoal())){
                availableExercisesHere.add(exercise);
            }
        }
        return availableExercisesHere;
    }

    private Set<LatLng> getPositions(List<Exercise> inExercises){
        Set<LatLng> positions = new HashSet<>();
        for(Exercise exercise:inExercises){
            positions.add(exercise.getStartPosition());
        }
        return positions;
    }
}
