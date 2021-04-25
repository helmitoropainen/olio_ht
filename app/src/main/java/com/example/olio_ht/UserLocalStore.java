package com.example.olio_ht;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class UserLocalStore {
    public static final String SP_NAME = "userInformation";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("firstName"+user.username, user.firstName);
        spEditor.putString("lastName"+user.username, user.lastName);
        spEditor.putString("username"+user.username, user.username);
        spEditor.putString("password"+user.username, user.password);
        spEditor.putString("salt"+user.username, user.salt);
        spEditor.putString("sex"+user.username, user.sex);
        spEditor.putString("dateOfBirth"+user.username, user.dateOfBirth.toString());
        spEditor.putInt("age"+user.username, user.age);
        spEditor.putFloat("height"+user.username, user.height);
        spEditor.putFloat("weight"+user.username, user.weight);
        spEditor.putFloat("bmi", user.bmi);
        spEditor.putLong("idealCalories"+user.username, user.idealCalories);
        spEditor.putLong("idealSleep"+user.username, user.idealSleep);
        spEditor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public User getUserInfo(String un) {
        String firstName = userLocalDatabase.getString("firstName"+un, "");
        String lastName = userLocalDatabase.getString("lastName"+un, "");
        String username = userLocalDatabase.getString("username"+un, "");
        String password = userLocalDatabase.getString("password"+un, "");
        String salt = userLocalDatabase.getString("salt"+un, "");
        String sex = userLocalDatabase.getString("sex"+un, "");
        LocalDate dateOfBirth = LocalDate.parse(userLocalDatabase.getString("dateOfBirth"+un, "2021-01-01"));
        int age = userLocalDatabase.getInt("age"+un, -1);
        float height = userLocalDatabase.getFloat("height"+un, -1);
        float weight = userLocalDatabase.getFloat("weight"+un, -1);
        /*float bmi = userLocalDatabase.getFloat("bmi"+un, -1);
        long idealCalories = userLocalDatabase.getLong("idealCalories"+un, -1);
        long idealSleep = userLocalDatabase.getLong("idealSleep"+un,-1);*/

        User user = new User(firstName, lastName, username, password, salt, sex, dateOfBirth, age,
                height, weight);

        return user;
    }

    // Tells which user is logged in.
    public String getUserLoggedIn() {
        String loggedInUser = userLocalDatabase.getString("loggedIn", "");
        return loggedInUser;
    }

    // Sets info to the userLocalDatabase which user is logged in.
    public void setUserLoggedIO(String un) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("loggedIn", un);
        spEditor.apply();
    }

}