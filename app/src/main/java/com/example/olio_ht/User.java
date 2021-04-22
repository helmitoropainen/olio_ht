package com.example.olio_ht;

import java.time.LocalDate;

public class User {
    String name, username, password, sex;
    String salt;
    LocalDate dateOfBirth;
    int age;
    float height, weight/*, bmi*/;
    // Pitiks tää bmi nyt olla täs? laitoin sen eka tänne, mut sit kommentoin sen pois. En oo kyl
    // laskenu sitä täällä missään.

    public User (String name, String username, String password, String salt, String sex,
                 LocalDate dateOfBirth, int age, float height, float weight/*, float bmi*/) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.height = height;
        this.weight = weight;
        // this.bmi = bmi;
    }
}