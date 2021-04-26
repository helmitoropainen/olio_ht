package com.example.olio_ht;

import static java.lang.Math.round;


public class FoodEntry extends CalorieEntry {


    String foodType;
    int amount;

    public FoodEntry(String ft, int a, double c) {
        foodType = ft;
        amount = a;
        calories = c;
    }

    public String getFoodType() { return foodType; }
    public int getFoodAmount() { return amount; }

    @Override
    public String getInfo() {
        if (foodType.equals("Own portion")) {
            info = foodType + " " + round(calories) + " kcal";
        } else {
            info = foodType + " " + amount + " g " + round(calories) + " kcal";
        }
        return info;
    }

    public void setFoodType(String ft) { foodType = ft; }
    public void setFoodAmount(int a) { amount = a; }

    public double countGainedCalories(FoodEntry fe, FoodData fd) {
        double calories;
        if (fe.getFoodType().equals("Own portion") == true) {
            calories = fe.getCalories();
        } else {
            calories = (double) amount * fd.getCalories() / 100;
        }
        fe.setCalories(calories);
        return calories;
    }
}
