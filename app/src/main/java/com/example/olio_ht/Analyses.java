package com.example.olio_ht;

import android.content.Context;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static java.lang.String.valueOf;


public class Analyses {

    String sleepGoal, calorieGoal ;
    String username ;
    User user ;
    String filename = "userData.csv" ;
    Context context = null ;
    UserLocalStore uls ;

    ArrayList<Entry> gainedCal = new ArrayList<>();
    ArrayList<Entry> lostCal = new ArrayList<>();
    ArrayList<Entry> calDiff = new ArrayList<>() ;
    ArrayList<BarEntry> sleep = new ArrayList<>();
    ArrayList<Entry> calorieG = new ArrayList<>() ;
    ArrayList<BarEntry> sleepG = new ArrayList<>();

    public Analyses(Context c) {
        context = c ;
        uls = new UserLocalStore(context) ;
    }
    public void retrieveUserAndGoals() {
        username = uls.getUserLoggedIn() ;
        user = uls.getUserInfo(username);
        sleepGoal = (String) valueOf(user.sleepGoal/60);
        calorieGoal = (String) valueOf(user.caloriesGoal);
    }

    // The method opens the csv file containing data on users and their entries. It picks the
    // user's entries from the latest days and saves them into an array list to be used as y-axis
    // values in the analytics graphs.

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

                calorieG.add(new Entry(Float.parseFloat(calorieGoal), i)) ;
                sleepG.add(new BarEntry(Float.parseFloat(sleepGoal), i));
;
            }

            LocalDate checkedDate ;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            int diff = 4 ;

            while ((line = br.readLine()) != null && diff > 0) {
                String[] data = line.split(";");

                if (data[0].equals(username) == true) {
                    checkedDate = LocalDate.parse(data[1], formatter) ;
                    diff = (int) Period.between(checkedDate, today).getDays();
                    System.out.println("DIFFERENCE IN DAYS: "+diff);

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
    }

    // In this method a string array is created, containing dates for the x-axis of the graphs
    public String[] createXAxisData() {
        LocalDate today = LocalDate.now();
        String[] xaxes = new String[5] ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");

        LocalDate date = today.plusDays(-4) ;
        String dateInString ;

        for (int i=0; i<5;i++) {
            dateInString = formatter.format(date) ;
            xaxes[i] = dateInString ;

            date = date.plusDays(1) ;
        }

        return xaxes ;
    }

    public ArrayList<Entry> getCalorieIntake() { return gainedCal; }

    public ArrayList<Entry> getCalorieLoss() { return lostCal; }

    public ArrayList<Entry> getCaloriesDifference() { return calDiff; }

    public ArrayList<BarEntry> getSleptHours() { return sleep ; }

    public ArrayList<Entry> getCalorieGoal() { return calorieG; }

    public ArrayList<BarEntry> getSleepGoal() { return sleepG ; }

}
