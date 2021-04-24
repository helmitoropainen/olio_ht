package com.example.olio_ht;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class EntryManager {

    private static EntryManager entryManager = new EntryManager();
    UserEntryLog userEntryLog;
    Entry sportEntries = null;
    Entry foodEntries = null;
    Entry sleepEntries = null;
    String filename = null;
    Context context = null;

    private EntryManager() {

    }

    public static EntryManager getInstance() {
        return entryManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEntries() {
        if (context != null && filename != null) {
            userEntryLog = new UserEntryLog(context);

            if (sportEntries != null && foodEntries != null && sleepEntries != null) {
                String username = sportEntries.getUsername();
                String birthday = sportEntries.getDate();

                double sleep = sleepEntries.getSum();
                double caloriesEaten = foodEntries.getSum();
                double caloriesBurned = sportEntries.getSum();

                userEntryLog.appendToCSV(filename, username, birthday, sleep, caloriesEaten, caloriesBurned);
            }

        }

    }


    public Entry getSportEntries() { return sportEntries; }
    public Entry getFoodEntries() { return foodEntries; }
    public Entry getSleepEntries() { return sleepEntries; }

    public void setSportEntries( Entry e ) { sportEntries = e; }
    public void setFoodEntries( Entry e ) { foodEntries = e; }
    public void setSleepEntries( Entry e ) { sleepEntries = e; }
    public void setFilename ( String f ) { filename = f; }
    public void setContext ( Context c ) { context = c; }

}
