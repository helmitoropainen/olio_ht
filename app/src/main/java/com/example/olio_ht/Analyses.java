package com.example.olio_ht;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Analyses {

    int calorieIntake, calorieLoss ;
    double sleptHours, sleepGoal ;
    long calorieGoal ;
    int date ;
    String user ;
    String filepath = "/data/user/0/com.example.olio_ht/files/userData.csv" ;
    Context context = this.context ;
    UserLocalStore uls = new UserLocalStore(context) ;

    public Analyses(int date) {
        this.date = date ;
    }

    public void retrieveUserLoggedIn() {
        user = uls.getUserLoggedIn() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void retrieveGoals() {
        sleepGoal = uls.getUserInfo(user).idealSleep;
        calorieGoal = uls.getUserInfo(user).idealCalories ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] readCSV() {
        java.time.LocalDate.now() ;
        String[] values = null ;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath)) ;
            String line = "" ;
            while ((line = br.readLine()) != null) {
                values = line.split(";") ;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return values ;
    }

    public int getCalorieIntake() {
        return calorieIntake;
    }

    public int getCalorieLoss() {
        return calorieLoss;
    }

    public double getSleptHours() {
        return sleptHours ;
    }

    public void setCalorieIntake(int c){ calorieIntake = c ; }

    public void setCalorieLoss(int c) { calorieLoss = c ; }

    public void setSleptHours(double h) { sleptHours= h ; }


}
