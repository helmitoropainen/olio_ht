package com.example.olio_ht;

// This class is made so RecyclerViewAdapter works
public class CalorieEntry extends UserEntry {

    String info;
    double calories;

    public CalorieEntry() {}

    public String getInfo() { return info; }

    public double getCalories() { return calories; }

    public void setCalories(double c) { calories = c; }

}
