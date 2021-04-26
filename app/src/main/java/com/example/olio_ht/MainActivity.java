package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    double sleepSum = 0, calorieSum = 0;
    boolean rqst1OK = false, rqst2OK = false;
    String filename, username;
    Bundle bundle = new Bundle();
    HomeFragment home = new HomeFragment();
    SettingsFragment settings = new SettingsFragment();
    PasswordFragment password = new PasswordFragment();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    UserEntryLog userEntryLog;
    UserLocalStore userLocalStore;
    EntryManager entryManager;
    User user;
    Entry sportEntry, foodEntry, sleepEntry;
    DatePickerDialog datePickerDialog;
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home, "home fragment").commit();

        userEntryLog = new UserEntryLog(this);
        filename = userEntryLog.createFile();

        entryManager = EntryManager.getInstance();
        entryManager.setFilename(filename);
        entryManager.setContext(this);

        userLocalStore = new UserLocalStore(this);
        username = userLocalStore.getUserLoggedIn();
        user = userLocalStore.getUserInfo(username);

        updateDate();
        loadState();
        home.updateCalorieSum(calorieSum);
        home.updateSleepSum(sleepSum);

    }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                sportEntry = (Entry) data.getSerializableExtra("sport entry");
                foodEntry = (Entry) data.getSerializableExtra("food entry");
                try {
                    calorieSum = (double) foodEntry.getSum() - sportEntry.getSum();
                    entryManager.setSportEntries(sportEntry);
                    entryManager.setFoodEntries(foodEntry);
                    if (home != null) {
                        home.updateCalorieSum(calorieSum);
                    }
                    rqst1OK = true;
                } catch (Exception e) {
                    rqst1OK = false;
                    Toast.makeText(this,"Adding data wasn't successful. Please re-try", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                sleepEntry = (Entry) data.getSerializableExtra("sleep entry");
                sleepSum = sleepEntry.getSum();
                entryManager.setSleepEntries(sleepEntry);
                if (home != null) {
                    home.updateSleepSum(sleepSum);
                }
                rqst2OK = true;
            } else {
                rqst2OK = false;
                Toast.makeText(this,"Adding data wasn't successful. Please re-try", Toast.LENGTH_LONG).show();
            }
        }

        if (rqst1OK && rqst2OK) {
            entryManager.saveEntries();
        }
        saveState();
    }

    public void saveState() {
        String spName = "shared preferences" + username;
        sharedPreferences = getSharedPreferences(spName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("sleep sum", String.valueOf(sleepSum));
        editor.putString("calorie sum", String.valueOf(calorieSum));
        editor.apply();
    }

    private void loadState() {
        String spName = "shared preferences" + username;
        sharedPreferences = getSharedPreferences(spName, MODE_PRIVATE);

        sleepSum = Double.parseDouble(sharedPreferences.getString("sleep sum", "0"));
        calorieSum = Double.parseDouble(sharedPreferences.getString("calorie sum", "0"));

    }

    @Override
    public void onBackPressed() {
    }
}