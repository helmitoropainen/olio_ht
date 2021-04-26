package com.example.olio_ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import static java.lang.Math.round;



public class HomeFragment extends Fragment {

    TextView factView, sleep, calories;
    View view;
    double sleepSum = 0, calorieSum = 0;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        factView = (TextView) this.view.findViewById(R.id.factView);
        sleep = (TextView) this.view.findViewById(R.id.goToSleep);
        calories = (TextView) this.view.findViewById(R.id.goToCalories);

        String[] facts = getResources().getStringArray(R.array.facts);
        String fact = facts[0];
        factView.setText("Did you know?\n" + fact);
        sleep.setText("You've slept " + String.format("%.2f", sleepSum) + " hours");
        calories.setText("Your calories are " + round(calorieSum) + " kcal");
    }

    public void changeFact() {
        if (getArguments() != null) {
            String arg = getArguments().getString("fact");
            factView = (TextView) this.view.findViewById(R.id.factView);
            factView.setText(arg);
        }
    }

    public void updateCalorieSum( double sum ) {
        calorieSum = sum;
        calories.setText("Your calories are " + round(calorieSum) + " kcal");
    }

    public void updateSleepSum( double sum ) {
        sleepSum = sum;
        sleep.setText("You've slept " + String.format("%.2f", sleepSum) + " hours");
    }
}

