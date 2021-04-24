package com.example.olio_ht;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;

public class UserEntryLog {
    Context context;
    UserLocalStore uls;

    public UserEntryLog(Context context) {
        this.context = context;
        uls = new UserLocalStore(context);
    }

    // Luo sen tiedoston, mis entryt eli tätä tarttee kuttuu vaan kerran
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String createFile() {
        System.out.println("CSV TIEDOSTOSIJAINTI: " + context.getFilesDir());
        String filename = "userData.csv";
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(filename,
                    Context.MODE_PRIVATE));
            String s = "username;date;sleep;caloriesEaten;caloriesBurned\n";
            osw.write(s);
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Nää oli testausta varten, saa hyödyntää entrymanagerissa.
        /*
        String username = uls.getUserLoggedIn();
        LocalDate birthday = uls.getUserInfo(username).dateOfBirth;
        double sleep = 8.2045;
        double caloriesEaten = 1869.2495;
        double caloriesBurned = 1503.456;

        appendToCSV(filename, username, birthday, sleep, caloriesEaten, caloriesBurned);
        appendToCSV(filename, username, birthday, sleep, caloriesEaten, caloriesBurned);

         */

        return filename;
    }

    // Tällä voi aina lisätä ne päivän tiedot.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void appendToCSV(String filename, String username, LocalDate date, double sleep, double caloriesEaten, double caloriesBurned) {
        try {
            String userdata = username+";"+date.toString()+";"+sleep+";"+caloriesEaten+";"+caloriesBurned+"\n";
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(filename,Context.MODE_APPEND));
            osw.write(userdata);
            osw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}