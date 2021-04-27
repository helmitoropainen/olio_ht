package com.example.olio_ht;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Analyses {

    double sleepGoal ;
    long calorieGoal ;
    String username ; //date,
    User user ;
    String filename = "userData.csv" ;
    Context context = null ;
    UserLocalStore uls ;
    LocalDate today ;

    ArrayList<Entry> gainedCal = new ArrayList<>();
    ArrayList<Entry> lostCal = new ArrayList<>();
    ArrayList<Entry> calDiff = new ArrayList<>() ;
    ArrayList<BarEntry> sleep = new ArrayList<>();

    public Analyses(Context c) {
        context = c ;
        uls = new UserLocalStore(context) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void retrieveUserAndGoals() {
        username = uls.getUserLoggedIn() ;
        user = uls.getUserInfo(username);
        sleepGoal = user.sleepGoal;
        calorieGoal = user.caloriesGoal;
    }

    public double getSleepGoal() { return sleepGoal ; }

    public long getCalorieGoal() { return calorieGoal; }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void readCSV() {
        try {
            String line = "" ;
            retrieveUserAndGoals();
            InputStream ins = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            for (int i=0;i<5;i++) {
                sleep.add(new BarEntry(Float.parseFloat("0"), i));
                gainedCal.add(new Entry(Float.parseFloat("0"), i));
                calDiff.add(new Entry(Float.parseFloat("0"), i)) ;
                lostCal.add(new Entry(Float.parseFloat("0"), i));
            }

            //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy") ;
            //date = sdf.format(new Date()) ;

            //long today = Calendar.getInstance().getTime().getTime() ;
            LocalDate checkedDate ;

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            //today = dateNow.format(formatter);

            int diff, check = 0 ;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");

                if (data[0].equals(username) == true) {
                    checkedDate = LocalDate.parse(data[1], formatter) ;

                    System.out.println("#################### Checked date " + checkedDate) ;
                    System.out.println("#################### Today " + today) ;

                    //diff = (int) Math.abs(java.time.temporal.ChronoUnit.DAYS.between(checkedDate, today));
                    diff = (int) Period.between(checkedDate, today).getDays();
                    System.out.println("PÄIVIEN EROTUS       " + diff);

                    if (diff < 5) {
                        sleep.set(4-diff, new BarEntry(Float.parseFloat(data[2]), 4-diff)) ;
                        gainedCal.set(4-diff, new Entry(Float.parseFloat(data[3]), 4-diff)) ;
                        lostCal.set(4-diff, new Entry((0-Float.parseFloat(data[4])), 4-diff)) ;
                        calDiff.set(4-diff, new Entry((Float.parseFloat(data[3]) - Float.parseFloat(data[4])), 4-diff)) ;
                    }
                    }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sleep) ;
        System.out.println(gainedCal) ;
        System.out.println(lostCal) ;
    }

    public ArrayList<Entry> getCalorieIntake() {
        return gainedCal;
    }

    public ArrayList<Entry> getCalorieLoss() {
        return lostCal;
    }

    public ArrayList<Entry> getCaloriesDifference() { return calDiff; }

    public ArrayList<BarEntry> getSleptHours() {
        return sleep ;
    }

}
