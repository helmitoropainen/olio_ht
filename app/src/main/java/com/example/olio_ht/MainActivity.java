package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int facts_index;
    //CharSequence username = "_user123";
    HomeFragment home = new HomeFragment();
    AnalyticsFragment analytics = new AnalyticsFragment();
    SettingsFragment settings = new SettingsFragment();
    PasswordFragment password = new PasswordFragment();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
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
        Bundle bundle = new Bundle();
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

    public void savePassword(View v) {

    }

    public void launchSleep(View v) {
        Intent intent = new Intent(MainActivity.this, SleepActivity.class);
        startActivityForResult(intent, 1);
    }

    public void launchCalories(View v) {
        Intent intent = new Intent(MainActivity.this, CalorieActivity.class);
        startActivityForResult(intent, 1);
    }

    // en saanu tätä et siin alapalkin asetusten kuvakkees lukis käyttäjänimi nii toimii
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settings_item = menu.findItem(R.id.nav_settings);
        System.out.println(settings_item.getTitle());
        settings_item.setTitle(username);
        return super.onPrepareOptionsMenu(menu);
    }*/
}