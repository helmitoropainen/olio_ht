package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    double sleepSum = 0, calorieSum = 0;
    String filename, username, date;
    HomeFragment home = new HomeFragment();
    SettingsFragment settings = new SettingsFragment();
    PasswordFragment password = new PasswordFragment();
    UserEntryLog userEntryLog;
    UserLocalStore userLocalStore;
    EntryManager entryManager;
    User user;
    UserEntry sportEntry = null, foodEntry = null, sleepEntry = null;
    DatePickerDialog datePickerDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // When app launches HomeFragment automatically opens
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home, "home fragment").commit();

        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        date = dateNow.format(formatter);

        userEntryLog = new UserEntryLog(this);
        filename = userEntryLog.createFile();

        entryManager = EntryManager.getInstance();
        entryManager.setFilename(filename);
        entryManager.setContext(this);

        userLocalStore = new UserLocalStore(this);
        username = userLocalStore.getUserLoggedIn();
        user = userLocalStore.getUserInfo(username);

        // Load Entrys and UI-state from SharedPrefrences
        updateDate();
        loadState();
        home.updateCalorieSum(calorieSum);
        home.updateSleepSum(sleepSum);

    }

    // Navigation between fragments, when HomeFragment is opend state is loaded again.
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            home = new HomeFragment();
                            selectedFragment = home;
                            loadState();
                            home.updateCalorieSum(calorieSum);
                            home.updateSleepSum(sleepSum);
                            break;
                        case R.id.nav_analytics :
                            selectedFragment = new AnalyticsFragment();
                            break;
                        case R.id.nav_settings:
                            settings = new SettingsFragment();
                            selectedFragment = settings;
                            updateDate();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    saveState();
                    return true;
                }
            };

    public void updateDate() {
        Bundle bundle = new Bundle();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                bundle.putString("date", date);
                settings.setArguments(bundle);
                settings.changeDate();
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
    }

    public String makeDateString( int day, int month, int year) {
        return day + "." + month + "." + year;
    }

    public void openDatePicker (View v) {
        datePickerDialog.show();
    }

    public void changePassword(View v) {
        getSupportFragmentManager().beginTransaction().remove(password).commit();
        PasswordFragment password = new PasswordFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.changePassword, password).commit();
    }

    public void launchCalories(View v) {
        Intent intent = new Intent(MainActivity.this, com.example.olio_ht.CalorieActivity.class);
        startActivityForResult(intent, 1);
    }

    public void launchSleep(View v) {
        Intent intent = new Intent(MainActivity.this, SleepActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If CalorieActivity sends entries back.
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                sportEntry = (UserEntry) data.getSerializableExtra("sport entry");
                foodEntry = (UserEntry) data.getSerializableExtra("food entry");
                try {
                    calorieSum = (double) foodEntry.getSum() - sportEntry.getSum();
                    entryManager.setSportEntries(sportEntry);
                    entryManager.setFoodEntries(foodEntry);
                    if (home != null) {
                        home.updateCalorieSum(calorieSum);
                    }
                } catch (Exception e) {
                    Toast.makeText(this,"Adding data wasn't successful. Please re-try", Toast.LENGTH_LONG).show();
                }
            }
        }

        // If SleepActivity sends an entry back.
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                sleepEntry = (UserEntry) data.getSerializableExtra("sleep entry");
                sleepSum = sleepEntry.getSum();
                entryManager.setSleepEntries(sleepEntry);
                if (home != null) {
                    home.updateSleepSum(sleepSum);
                }
            } else {
                Toast.makeText(this,"Adding data wasn't successful. Please re-try", Toast.LENGTH_LONG).show();
            }
        }
        // Send entries to EntryManager which updates log.
        entryManager.saveEntries();
        saveState();
    }

    // Save username, sums and entries in SharedPrefrences
    public void saveState() {
        String spName = "shared preferences" + username;
        sharedPreferences = getSharedPreferences(spName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("sleep sum", String.valueOf(sleepSum));
        editor.putString("calorie sum", String.valueOf(calorieSum));
        Gson gsonSleep = new Gson();
        Gson gsonFood = new Gson();
        Gson gsonSport = new Gson();
        String jsonSleep = gsonSleep.toJson(sleepEntry);
        String jsonFood = gsonFood.toJson(foodEntry);
        String jsonSport = gsonSport.toJson(sportEntry);
        editor.putString("sleep entry", jsonSleep);
        editor.putString("food entry", jsonFood);
        editor.putString("sport entry", jsonSport);
        editor.apply();
    }

    // Get sums and entries from SharedPreferences and if entries are null set default values
    private void loadState() {
        String spName = "shared preferences" + username;
        sharedPreferences = getSharedPreferences(spName, MODE_PRIVATE);
        sleepSum = Double.parseDouble(sharedPreferences.getString("sleep sum", "0"));
        calorieSum = Double.parseDouble(sharedPreferences.getString("calorie sum", "0"));
        Gson gsonSleep = new Gson();
        Gson gsonFood = new Gson();
        Gson gsonSport = new Gson();
        String jsonSleep = sharedPreferences.getString("sleep entry", null);
        String jsonFood = sharedPreferences.getString("food entry", null);
        String jsonSport = sharedPreferences.getString("sport entry", null);

        Type type = new TypeToken<UserEntry>() {}.getType();

        sleepEntry = gsonSleep.fromJson(jsonSleep, type);
        foodEntry = gsonFood.fromJson(jsonFood, type);
        sportEntry = gsonSport.fromJson(jsonSport, type);

        if (sleepEntry == null) {
            sleepEntry = new UserEntry();
            sleepEntry.setDate(date);
            sleepEntry.setUsername(username);
            sleepEntry.setSum(0);
        }
        if (foodEntry == null) {
            foodEntry = new UserEntry();
            foodEntry.setDate(date);
            foodEntry.setUsername(username);
            foodEntry.setSum(0);
        }
        if (sportEntry == null) {
            sportEntry = new UserEntry();
            sportEntry.setDate(date);
            sportEntry.setUsername(username);
            sportEntry.setSum(0);
        }
    }

    @Override
    public void onBackPressed() {
    }
}