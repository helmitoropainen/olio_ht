package com.example.olio_ht;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserLocalStore {
    public static final String SP_NAME = "userInformation";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name"+user.username, user.name);
        spEditor.putString("username"+user.username, user.username);
        spEditor.putString("password"+user.username, user.password);
        spEditor.putString("salt"+user.username, user.salt);
        spEditor.putString("sex"+user.username, user.sex);
        spEditor.putString("dateOfBirth"+user.username, user.dateOfBirth.toString());
        spEditor.putInt("age"+user.username, user.age);
        spEditor.putFloat("height"+user.username, user.height);
        spEditor.putFloat("weight"+user.username, user.weight);
        // spEditor.putFloat("bmi", user.bmi);
        spEditor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public User getUserInfo(String un) {
        String name = userLocalDatabase.getString("name"+un, "");
        String username = userLocalDatabase.getString("username"+un, "");
        String password = userLocalDatabase.getString("password"+un, "");
        String salt = userLocalDatabase.getString("salt"+un, "");
        String sex = userLocalDatabase.getString("sex"+un, "");
        LocalDate dateOfBirth = LocalDate.parse(userLocalDatabase.getString("dateOfBirth"+un, ""));
        int age = userLocalDatabase.getInt("age"+un, -1);
        float height = userLocalDatabase.getFloat("height"+un, -1);
        float weight = userLocalDatabase.getFloat("weight"+un, -1);
        // float bmi = userLocalDatabase.getFloat("bmi", -1);

        User user = new User(name, username, password, salt, sex, dateOfBirth, age, height, weight
                /*, bmi*/);

        return user;
    }

    // Tells if a user is logged in or out.
    public boolean getUserLoggedIn(String un) {
        if (userLocalDatabase.getBoolean("loggedIn"+un, false)) {
            return true;
        } else {
            return false;
        }
    }

    // Sets info to the userLocalDatabase which user is logged in.
    public void setUserLoggedIn(boolean loggedIn, String un) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn"+un, loggedIn);
        spEditor.apply();
    }

}