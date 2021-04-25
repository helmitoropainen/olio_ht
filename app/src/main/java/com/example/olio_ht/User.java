package com.example.olio_ht;
import java.io.Serializable;
import java.time.LocalDate;

public class User {
    String firstName, lastName, username, password, sex;
    String salt;
    LocalDate dateOfBirth;
    long idealSleep, idealCalories;
    long sleepGoal, caloriesGoal;
    int age;
    float height, weight, bmi;
    public User (String firstName, String lastName, String username, String password, String salt,
                 String sex, LocalDate dateOfBirth, int age, float height, float weight,
                 long caloriesGoal, long sleepGoal) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.caloriesGoal = caloriesGoal;
        this.sleepGoal = sleepGoal;
    }

    public void setBMI() {
        bmi = weight/((height/100)*(height/100));
        System.out.println("LASKETTU BMI: "+bmi);
    }

    public void setIdealCalories() {
        if (sex.equals("Female")) {
            idealCalories = 2000;
        } else if (sex.equals("Male")) {
            idealCalories = 2500;
        } else {
            idealCalories = (2500+2000)/2;
        }
    }
    public void setIdealSleep() {
        if (age < 1) {
            idealSleep = (17+14)/2;
        } else if (age >= 1 || age < 2) {
            idealSleep = 10+4;
        } else if (age >= 1 || age < 3) {
            idealSleep = (11+12)/2+(1+2)/2;
        } else if (age >= 3 || age < 6) {
            idealSleep = (10+13)/2;
        } else if (age >= 6 || age < 14) {
            idealSleep = (9+11)/2;
        } else if (age >= 14 || age < 18) {
            idealSleep = (8+10)/2;
        } else if (age >= 18) {
            idealSleep = (7+9)/2;
        }
        System.out.println("LASKETTU UNI: "+idealSleep);
    }

    public void setCaloriesGoal() {
        caloriesGoal = idealCalories;
    }
    public void setSleepGoal() {
        sleepGoal = idealSleep;
    }
}