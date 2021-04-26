package com.example.olio_ht;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class Analyses {

    double sleepGoal ;
    long calorieGoal ;
    String date, username;
    User user ;
    String filename = "userData.csv" ;
    Context context = null ;

    ArrayList<Entry> gainedCal = new ArrayList<>();
    ArrayList<Entry> lostCal = new ArrayList<>();
    ArrayList<Entry> sleep = new ArrayList<>();

    public Analyses(Context c) {
        context = c ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void retrieveUserAndGoals() {
        UserLocalStore uls = new UserLocalStore(context) ;

        username = uls.getUserLoggedIn() ;
        user = uls.getUserInfo(username);
        sleepGoal = user.sleepGoal;
        calorieGoal = user.caloriesGoal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void readCSV() {
        try {
            String line = "" ;

            retrieveUserAndGoals();

            InputStream ins = context.openFileInput(filename); // täl ei tuu file not found erroria
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy") ;
            date = sdf.format(new Date()) ;

            Calendar c = Calendar.getInstance() ;
            int i = 0 ;
            while ((line = br.readLine()) != null && i<5) {
                String[] data = line.split(";");
                if (data[0].equals(username) == true) {//&& data[1].equals(date) == true) { täs ehdos jotain mätää XD
                    System.out.println("match!");
                    sleep.add(new Entry(Float.parseFloat(data[2]), i)) ;
                    gainedCal.add(new Entry(Float.parseFloat(data[3]), i)) ;
                    lostCal.add(new Entry(Float.parseFloat(data[4]), i)) ;

                    c.setTime(sdf.parse(date)) ;
                    c.add(Calendar.DAY_OF_MONTH, -1) ;
                    date = sdf.format(c.getTime()) ;
                    i++ ;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Entry> getCalorieIntake() {
        return gainedCal;
    }

    public ArrayList<Entry> getCalorieLoss() {
        return lostCal;
    }

    public ArrayList<Entry> getSleptHours() {
        return sleep ;
    }

}
