package com.example.olio_ht;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class PopUpSleep extends Activity {

    Button back;
    EditText goalInputH, goalInputMin;
    TextView viewGoal, viewIdeal;
    int goalh, goalmin;
    long goal, ideal;
    String username;
    User user;
    UserLocalStore userLocalStore;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        if (goalmin == 0) {
            viewGoal.setText("Your nightly goal is set to " + goalh + " hours");
            goal = goalh;
        } else {
            viewGoal.setText("Your nightly goal is set to\n" + goalh + " hours and " + goalmin + " minutes");
            goal = goalh + goalmin / 60;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopUp();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void changeGoal(View v) {
        if (goalInputH.getText().toString().trim().length() > 0) {
            goalh = Integer.parseInt(goalInputH.getText().toString());
            goalmin = Integer.parseInt(goalInputMin.getText().toString());
            if (goalh > 23 || goalmin > 59) {
                Toast.makeText(getApplicationContext(), "Goal is above limit!", Toast.LENGTH_SHORT).show();
            } else {
                if (goalmin == 0) {
                    viewGoal.setText("Your daily goal is set to " + goalh + " hours");
                    goal = goalh;
                } else {
                    viewGoal.setText("Your daily goal is set to\n" + goalh + " hours and " + goalmin + " minutes");
                    goal = goalh + goalmin / 60;
                }

                userLocalStore.getUserInfo(username);
                User changedUser = new User(user.firstName, user.lastName, user.username, user.password,
                        user.salt, user.sex, user.dateOfBirth, user.age, user.height, user.weight, user.caloriesGoal, goal);
                userLocalStore.storeUserData(changedUser);
            }
        }
    }

    public void setViewIdeal() {
        ideal = user.idealCalories;
        viewIdeal.setText("Your recommended nightly sleep is " + ideal + " hours");
    }

    public void closePopUp() {
        finish();
    }

    @Override
    public void onBackPressed() {
        closePopUp();
    }

}
