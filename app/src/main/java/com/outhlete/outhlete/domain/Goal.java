package com.outhlete.outhlete.domain;

public enum Goal {
    COOL_DOWN("Cool down", "Full body"),
    STRETCHING_TOP_BODY("Stretching", "Lower body"),
    STRETCHING_LOW_BODY("Stretching", "Upper body"),
    STRETCHING_CORE_BODY("Stretching", "Core"),
    CARDIO("Cardio", "Full body"),,
    WARM_UP ("Warm up", "Full body"),
    ABS ("Muscle growth", "Abs"),
    LEGS("Muscle growth", "Legs"),
    CHEST("Muscle growth", "Chest"),
    ARMS("Muscle growth", "Arms"),
    BACK("Muscle growth", "Back");

    private String description;
    private String bodyPart;

    Goal(String description, String bodyPart){
        this.description = description;
        this.bodyPart = bodyPart;
    }



}
