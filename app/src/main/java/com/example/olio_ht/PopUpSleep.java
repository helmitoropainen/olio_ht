package com.example.olio_ht;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PopUpSleep extends Activity {

    Button back;
    EditText goalInputH, goalInputMin;
    TextView viewGoal, viewIdeal;
    Intent intent = new Intent();
    long goalh = 0, goalmin = 0;
    long goal, ideal;
    String username;
    boolean hasUserChangedGoal = false;
    User user;
    UserLocalStore userLocalStore;

    // Popup is opened and it informs the user about their own goals and recommendations based on
    // their age.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindowsleep);

        back = (Button) findViewById(R.id.buttonClosePop);
        goalInputH = (EditText) findViewById(R.id.goalinputh);
        goalInputMin = (EditText) findViewById(R.id.goalinputmin);
        viewGoal = (TextView) findViewById(R.id.viewGoal);
        viewIdeal = (TextView) findViewById(R.id.viewIdeal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        userLocalStore = new UserLocalStore(this);
        username = userLocalStore.getUserLoggedIn();
        user = userLocalStore.getUserInfo(username);

        setViewIdeal();
        goal = user.sleepGoal;
        goalh = goal/60;
        goalmin = goal%60;

        if (goalmin == 0) {
            viewGoal.setText("Your nightly goal is set to " + goalh + " hours");
            goal = goalh;
        } else {
            viewGoal.setText("Your nightly goal is set to " + goalh + " hours and " + goalmin + " minutes");
            goal = goalh + goalmin / 60;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopUp();
            }
        });

    }

    //  This method registers the user's new goal, once they set or change it.
    public void changeGoal(View v) {
        if (goalInputH.getText().toString().trim().length() > 0) {
            if (goalInputMin.getText().toString().trim().length() <= 0) {
                goalInputMin.setText("0");
            }
            goalh = Integer.parseInt(goalInputH.getText().toString());
            goalmin = Integer.parseInt(goalInputMin.getText().toString());
            if (goalh > 23 || goalmin > 59) {
                Toast.makeText(getApplicationContext(), "Goal is above limit!", Toast.LENGTH_SHORT).show();
            } else {
                if (goalmin == 0) {
                    viewGoal.setText("Your nightly goal is set to " + goalh + " hours");
                    goal = goalh * 60;
                } else {
                    viewGoal.setText("Your nightly goal is set to " + goalh + " hours and " + goalmin + " minutes");
                    goal = goalh * 60 + goalmin;
                }

                userLocalStore.getUserInfo(username);
                User changedUser = new User(user.firstName, user.lastName, user.username, user.password,
                        user.salt, user.sex, user.dateOfBirth, user.age, user.height, user.weight, user.caloriesGoal, goal);
                userLocalStore.storeUserData(changedUser);

                hasUserChangedGoal = true;
            }
        } else {
            Toast.makeText(this, "Please enter hours", Toast.LENGTH_SHORT).show();
        }
    }

    // The user's ideal sleep is retrieved and displayed
    public void setViewIdeal() {
        ideal = user.idealSleep;
        viewIdeal.setText("Your recommended nightly sleep is " + ideal + " hours");
    }

    // HELMI
    public void closePopUp() {
        if (hasUserChangedGoal) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        closePopUp();
    }
}
