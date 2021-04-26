package com.example.olio_ht;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

            if (sportEntries != null && foodEntries != null && sleepEntries != null &&
                    (sportEntries.getDate().equals(foodEntries.getDate()) && sportEntries.getDate().equals(sleepEntries.getDate())) &&
                    (sportEntries.getUsername().equals(foodEntries.getUsername()) && sportEntries.getUsername().equals(sleepEntries.getUsername()))) {
                String username = sportEntries.getUsername();
                String date = sportEntries.getDate();

                double sleep = sleepEntries.getSum();
                double caloriesEaten = foodEntries.getSum();
                double caloriesBurned = sportEntries.getSum();

                try {
                    String filepath = context.getFilesDir() + "/" + filename;
                    List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8));
                    String s = "";
                    String replace = null;
                    String newline = username+";"+date+";"+sleep+";"+caloriesEaten+";"+caloriesBurned;
                    InputStream ins = context.openFileInput(filename);
                    BufferedReader br = new BufferedReader(new InputStreamReader(ins));
                    while ((s=br.readLine()) != null) {
                        String[] data;
                        data = s.split(";");
                        if (data[0].equals(username) && data[1].equals(date)) {
                            replace = s;
                            for (int i = 0; i < fileContent.size(); i++) {
                                if (fileContent.get(i).equals(replace)) {
                                    fileContent.set(i, newline);
                                    break;
                                }
                            }
                            Files.write(Paths.get(filepath), fileContent, StandardCharsets.UTF_8);
                        }
                    }

                    if (replace == null) {
                        userEntryLog.appendToCSV(filename, username, date, sleep, caloriesEaten, caloriesBurned);
                    }

                }  catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setSportEntries( Entry e ) { sportEntries = e; }
    public void setFoodEntries( Entry e ) { foodEntries = e; }
    public void setSleepEntries( Entry e ) { sleepEntries = e; }
    public void setFilename ( String f ) { filename = f; }
    public void setContext ( Context c ) { context = c; }

}
