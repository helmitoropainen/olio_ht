package com.example.olio_ht;

import java.io.Serializable;


public class CalorieEntry extends Entry {


    String info;
    double calories;

    public CalorieEntry() {}

    public String getInfo() { return info; }

    public double getCalories() { return calories; }

    public void setCalories(double c) { calories = c; }

}
