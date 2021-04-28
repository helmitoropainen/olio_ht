package com.example.olio_ht;

import android.content.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class UserEntryLog {

    Context context;
    UserLocalStore uls;

    public UserEntryLog(Context context) {
        this.context = context;
        uls = new UserLocalStore(context);
    }

    // Creates the log where entries are saved from every user and returns the name of the file.
    public String createFile() {
        String filename = "userData.csv";
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
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
        }
        return filename;
    }

    // Takes filename, username, current date, hours slept, calorie intake and burned calories as
    // input values and appends them into the log.
    public void appendToCSV(String filename, String username, String date, double sleep, double caloriesEaten, double caloriesBurned) {
        try {
            String userdata = username+";"+date+";"+sleep+";"+caloriesEaten+";"+caloriesBurned+"\n";
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(filename,Context.MODE_APPEND));
            osw.write(userdata);
            osw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
