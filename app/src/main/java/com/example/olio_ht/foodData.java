package com.example.olio_ht;

import static java.lang.Math.round;

public class foodData {

    String foodName;
    long calories;

    public foodData() {}

    public void setFoodName(String fn) { foodName = fn; }
    public void setCalories(double c) { calories = round(c); }

    public String getFoodName() { return foodName; }
    public long getCalories() { return calories; }

    @Override
    public String toString() {
        return foodName;
    }
}
