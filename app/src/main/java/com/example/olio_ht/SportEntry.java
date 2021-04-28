package com.example.olio_ht;

import static java.lang.Math.round;

public class SportEntry extends CalorieEntry {

    String sportType;
    int duration;

    public SportEntry(String ft, int a, double c) {
        sportType = ft;
        duration = a;
        calories = c;
    }

    public String getSportType() { return sportType; }

    // Method creates and returns line that is printed in RecyclerView
    @Override
    public String getInfo() {
        if (sportType.equals("Own workout")) {
            info = sportType + " " + round(calories) + " kcal";
        } else {
            info = sportType + " " + duration + " min " + round(calories) + " kcal";
        }
        return info;
    }

    public void setSportType(String ft) { sportType = ft; }
    public void setDuration(int a) { duration = a; }

    // Method get's SportEntry, SportData (from spinner choice) and user's weight as input arguments,
    // uses SportData, weight and attribute duration to count spent calories and sets countedCalories
    // to SportEntry, also returns counted calories.
    public double countSpentCalories(SportEntry se, SportData sd, float w) {
        double countedCalories;
        if (se.getSportType().equals("Own workout") == true) {
            countedCalories = se.getCalories();
        } else {
            countedCalories = (double) (Double.valueOf(duration)/60) * sd.getCalories() * w ;
        }
        se.setCalories(countedCalories);
        return countedCalories;
    }
}
