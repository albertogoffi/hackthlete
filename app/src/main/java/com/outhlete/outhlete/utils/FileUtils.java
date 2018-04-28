package com.outhlete.outhlete.utils;

import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.opencsv.CSVReader;
import com.outhlete.outhlete.R;
import com.outhlete.outhlete.domain.Exercise;
import com.outhlete.outhlete.domain.Goal;
import com.outhlete.outhlete.domain.PubliBikeStation;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<Exercise> loadExercisesFromCSV(){
        List<Exercise> exercises = new ArrayList<>();
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(Resources.getSystem().openRawResource(R.raw.exercises)));//Specify asset file name
            //skip first line
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                double startLat = Double.valueOf(line[2]);
                double startLng = Double.valueOf(line[3]);
                double endLat = Double.valueOf(line[4]);
                double endLng = Double.valueOf(line[5]);
                LatLng start = new LatLng(startLat, startLng);
                LatLng end = new LatLng(endLat, endLng);
                Goal goal;
                switch(line[8]) {
                    case "STRETCHING_TOP_BODY":
                        goal = Goal.STRETCHING_TOP_BODY;
                        break;
                    case "STRETCHING_LOW_BODY":
                        goal = Goal.STRETCHING_LOW_BODY;
                        break;
                    case "STRETCHING_CORE_BODY":
                        goal = Goal.STRETCHING_CORE_BODY;
                        break;
                    case "ABS":
                        goal = Goal.ABS;
                        break;
                    case "LEGS":
                        goal = Goal.LEGS;
                        break;
                    case "BACK":
                        goal = Goal.BACK;
                        break;
                    case "CHEST":
                        goal = Goal.CHEST;
                        break;
                    case "ARMS":
                        goal = Goal.ARMS;
                        break;
                    default:
                        throw new RuntimeException("error");
                }
                Exercise exercise = new Exercise(line[0], line[1], start, end, Integer.valueOf(line[6]), line[7], goal);
                exercises.add(exercise);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return exercises;
    }

    public  static List<PubliBikeStation> loadPubliBikeStationsFromCSV(){
        List<PubliBikeStation> stations = new ArrayList<>();
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(Resources.getSystem().openRawResource(R.raw.publibike_stations)));//Specify asset file name
            //skip first line
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                double lat = Double.valueOf(line[1]);
                double lng = Double.valueOf(line[2]);
                LatLng pos = new LatLng(lat, lng);

                PubliBikeStation station = new PubliBikeStation(pos);
                stations.add(station);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return stations;
    }
}
