package com.example.olio_ht;

import static java.lang.Math.round;

public class foodEntry extends calorieEntry {

    String foodType, info;
    int amount;
    double gainedCalories;

    public foodEntry(String ft, int a, double gc) {
        foodType = ft;
        amount = a;
        gainedCalories = gc;
    }

    public String getFoodType() { return foodType; }
    public int getFoodAmount() { return amount; }

    @Override
    public double getCalories() { return gainedCalories; }

    @Override
    public String getInfo() {
        if (foodType.equals("Own portion")) {
            info = foodType + " " + round(gainedCalories) + " kcal";
        } else {
            info = foodType + " " + amount + " g " + round(gainedCalories) + " kcal";
        }
        return info;
    }

    public void setFoodType(String ft) { foodType = ft; }
    public void setFoodAmount(int a) { amount = a; }
    public void setFoodCalories(double gc) { gainedCalories = gc; }

    public double countGainedCalories(foodEntry fe, foodData fd) {
        double calories;
        if (fe.getFoodType().equals("Own portion") == true) {
            calories = fe.getCalories();
        } else {
            calories = (double) amount * fd.getCalories() / 100;
        }
        fe.setFoodCalories(calories);
        return calories;
    }
}
