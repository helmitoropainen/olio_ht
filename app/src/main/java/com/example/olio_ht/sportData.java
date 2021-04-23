package com.example.olio_ht;

import static java.lang.Math.round;

public class sportData {

    String sportName;
    double calories;

    public sportData() {}

    public void setSportName(String fn) { sportName = fn; }
    public void setCalories(double c) { calories = c; }

    public String getSportName() { return sportName; }
    public double getCalories() { return calories; }

    @Override
    public String toString() { return sportName; }
}

