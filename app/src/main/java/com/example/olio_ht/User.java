package com.example.olio_ht;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    String firstName, lastName, username, password, sex;
    String salt;
    LocalDate dateOfBirth;
    long idealSleep, idealCalories;
    int age;
    float height, weight, bmi;

    public User (String firstName, String lastName, String username, String password, String salt,
                 String sex, LocalDate dateOfBirth, int age, float height, float weight) {
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
        this.bmi = setBMI();
        this.idealCalories = setIdealCalories();
        this.idealSleep = setIdealSleep();
    }

    public float setBMI() {
        float bmi = 0;
        return bmi;
    }

    public long setIdealCalories() {
        long idealCalories = 0;

        return idealCalories;
    }

    public long setIdealSleep() {
        long idealSleep = 0;
        return idealSleep;
    }
}