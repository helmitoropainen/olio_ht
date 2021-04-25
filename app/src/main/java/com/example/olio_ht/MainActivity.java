package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int facts_index;
    double sleepSum = 0, calorieSum = 0;
    boolean rqst1OK = false, rqst2OK = false;
    String filename, username;
    Bundle bundle = new Bundle();
    HomeFragment home = new HomeFragment();
    AnalyticsFragment analytics = new AnalyticsFragment();
    SettingsFragment settings = new SettingsFragment();
    PasswordFragment password = new PasswordFragment();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    UserEntryLog userEntryLog;
    UserLocalStore userLocalStore;
    EntryManager entryManager;
    User user;
    Entry sportEntry, foodEntry, sleepEntry;

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

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = home;
                            break;
                        case R.id.nav_analytics :
                            selectedFragment = analytics;
                            break;
                        case R.id.nav_settings:
                            selectedFragment = settings;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public void randomFact (View v) {
        String[] facts = getResources().getStringArray(R.array.facts);
        int min = 0;
        int max = facts.length - 1;
        Random rand = new Random();
        facts_index = rand.nextInt((max - min) + 1) + min;
        String text = String.format("Did you know?\n" + facts[facts_index]);
        bundle.putString("fact", text);
        home.setArguments(bundle);
        home.changeFact();
    }

    public void updateDate(View v) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Bundle bundle = new Bundle();

        DatePickerDialog dialog = new DatePickerDialog(
                MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "." + month + "." + year;
                bundle.putString("date", date);
                settings.setArguments(bundle);
                settings.changeDate();
            }
        };
    }

    public void changePassword(View v) {
        getSupportFragmentManager().beginTransaction().remove(password).commit();
        PasswordFragment password = new PasswordFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.changePassword, password).commit();
    }

    public void launchCalories(View v) {
        Intent intent = new Intent(MainActivity.this, CalorieActivity.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, 1);
    }

    public void launchSleep(View v) {
        Intent intent = new Intent(MainActivity.this, SleepActivity.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                sportEntry = (Entry) data.getSerializableExtra("sport entry");
                foodEntry = (Entry) data.getSerializableExtra("food entry");
                try {
                    calorieSum = (double) foodEntry.getSum() - sportEntry.getSum();
                    System.out.println("#### food sum " + foodEntry.getSum());
                    System.out.println("#### sport sum -" + sportEntry.getSum());
                    System.out.println("#### calorie sum " + calorieSum);
                    entryManager.setSportEntries(sportEntry);
                    entryManager.setFoodEntries(foodEntry);
                    home = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home fragment");
                    if (home != null) {
                        home.updateCalorieSum(calorieSum);
                    }
                    rqst1OK = true;
                    Toast.makeText(this,"Adding data was successful.", Toast.LENGTH_LONG).show();
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
                home = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home fragment");
                if (home != null) {
                    home.updateSleepSum(sleepSum);
                }
                rqst2OK = true;
                Toast.makeText(this,"Adding data was successful.", Toast.LENGTH_LONG).show();
            } else {
                rqst2OK = false;
                Toast.makeText(this,"Adding data wasn't successful. Please re-try", Toast.LENGTH_LONG).show();
            }
        }

        if (rqst1OK == true && rqst2OK == true) {
            entryManager.saveEntries();
        }
    }

}