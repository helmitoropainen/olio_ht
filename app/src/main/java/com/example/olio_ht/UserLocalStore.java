package com.example.olio_ht;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;
public class UserLocalStore {
    public static final String SP_NAME = "userInformation";
    SharedPreferences userLocalDatabase;
    String firstName;
    String lastName;
    String username;
    String password;
    String salt;
    String sex;
    LocalDate dateOfBirth;
    int age;
    float height;
    float weight;
    float bmi;
    long idealCalories;
    long idealSleep;
    long sleepGoal;
    long caloriesGoal;

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
        spEditor.putFloat("bmi"+user.username, user.bmi);
        spEditor.putLong("idealCalories"+user.username, user.idealCalories);
        spEditor.putLong("idealSleep"+user.username, user.idealSleep);
        spEditor.putLong("sleepGOal"+user.username, user.sleepGoal);
        spEditor.putLong("caloriesGoal"+user.username, user.caloriesGoal);
        spEditor.apply();
    }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public User getUserInfo(String un) {
            firstName = userLocalDatabase.getString("firstName"+un, "");
            lastName = userLocalDatabase.getString("lastName"+un, "");
            username = userLocalDatabase.getString("username"+un, "");
            password = userLocalDatabase.getString("password"+un, "");
            salt = userLocalDatabase.getString("salt"+un, "");
            sex = userLocalDatabase.getString("sex"+un, "");
            dateOfBirth = LocalDate.parse(userLocalDatabase.getString("dateOfBirth"+un, "2021-01-01"));
            age = userLocalDatabase.getInt("age"+un, -1);
            height = userLocalDatabase.getFloat("height"+un, -1);
            weight = userLocalDatabase.getFloat("weight"+un, -1);
            bmi = userLocalDatabase.getFloat("bmi"+un, -1);
            idealCalories = userLocalDatabase.getLong("idealCalories"+un, -1);
            idealSleep = userLocalDatabase.getLong("idealSleep"+un,-1);
            caloriesGoal = userLocalDatabase.getLong("caloriesGoal"+un, -1);
            sleepGoal = userLocalDatabase.getLong("sleepGoal"+un, -1);

            User user = new User(firstName, lastName, username, password, salt, sex, dateOfBirth, age,
                    height, weight, caloriesGoal, sleepGoal);
            user.setIdealSleep();
            user.setIdealCalories();
            user.setBMI();

            return user;
        }




<<<<<<< HEAD
=======
        User user = new User(firstName, lastName, username, password, salt, sex, dateOfBirth, age,
                height, weight, caloriesGoal, sleepGoal);
>>>>>>> parent of f1c2937 (Merge branch 'master' into Helmin-branch)




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