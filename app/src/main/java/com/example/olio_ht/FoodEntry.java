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

    // Method creates and returns line that is printed in recycler view
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

    // Method get's FoodEntry and FoodData (from spinner choice) as input arguments, uses FoodData
    // and attribute amount to count gained calories and sets gainedCalories to FoodEntry, also
    // returns countedCalories.
    public double countGainedCalories(FoodEntry fe, FoodData fd) {
        double countedCalories;
        if (fe.getFoodType().equals("Own portion") == true) {
            countedCalories = fe.getCalories();
        } else {
            countedCalories = (double) amount * fd.getCalories() / 100;
        }
        fe.setCalories(calories);
        return countedCalories;
    }
}
