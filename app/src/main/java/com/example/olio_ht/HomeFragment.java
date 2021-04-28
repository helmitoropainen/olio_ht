package com.example.olio_ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Random;
import static java.lang.Math.round;

public class HomeFragment extends Fragment {

    TextView factView, sleep, calories;
    View view;
    double sleepSum = 0, calorieSum = 0;
    int facts_index;

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

        randomFact();
        sleep.setText("You've slept " + String.format("%.2f", sleepSum) + " hours");
        calories.setText("The sum of your calories is " + round(calorieSum) + " kcal");

        factView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomFact();
            }
        });
    }

    public void updateCalorieSum( double sum ) {
        calorieSum = sum;
        try {
            calories.setText("The sum of your calories is " + round(calorieSum) + " kcal");
        } catch (Exception e) {}
    }

    public void updateSleepSum( double sum ) {
        sleepSum = sum;
        try {
            sleep.setText("You've slept " + String.format("%.2f", sleepSum) + " hours");
        } catch (Exception e) {}

    }

    // Randomizes and sets fact from facts-array in strings.xml folder.
    public void randomFact () {
        String[] facts = getResources().getStringArray(R.array.facts);
        int min = 0;
        int max = facts.length - 1;
        Random rand = new Random();
        facts_index = rand.nextInt((max - min) + 1) + min;
        factView.setText("Did you know?\n" + facts[facts_index]);
    }
}

