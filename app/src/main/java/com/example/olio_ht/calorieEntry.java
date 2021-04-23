package com.example.olio_ht;

import java.util.ArrayList;

public abstract class calorieEntry {

    String info;
    ArrayList<calorieEntry> array;
    double calories;

    public calorieEntry() {}

    public String getInfo() { return info; }

    public double getCalories() { return calories; }

    public void addToList(calorieEntry ce) { array.add(ce); }

}
