package com.example.olio_ht;

import static java.lang.Math.round;

public class sportEntry extends calorieEntry {

    String sportType, info;
    int duration;
    double spentCalories;

    public sportEntry(String ft, int a, double gc) {
        sportType = ft;
        duration = a;
        spentCalories = gc;
    }

    public String getSportType() { return sportType; }
    public int getDuration() { return duration; }

    public double getCalories() { return spentCalories; }

    @Override
    public String getInfo() {
        if (sportType.equals("Own workout")) {
            info = sportType + " " + round(spentCalories) + " kcal";
        } else {
            info = sportType + " " + duration + " min " + round(spentCalories) + " kcal";
        }
        return info;
    }

    public void setSportType(String ft) { sportType = ft; }
    public void setDuration(int a) { duration = a; }
    public void setSpentCalories(double gc) { spentCalories = gc; }

    public double countSpentCalories(sportEntry se, sportData sd, int w) {
        double calories;
        if (se.getSportType().equals("Own workout") == true) {
            calories = se.getCalories();
        } else {
            calories = (double) (Double.valueOf(duration)/60) * sd.getCalories() * w ;
        }
        se.setSpentCalories(calories);
        return calories;
    }
}
