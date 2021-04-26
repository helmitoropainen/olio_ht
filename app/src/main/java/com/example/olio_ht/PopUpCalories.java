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

public class PopUpCalories extends Activity {


    Button back;
    EditText goalInput;
    TextView viewGoal, viewIdeal;
    long goal, ideal;
    String username;
    User user;
    UserLocalStore userLocalStore;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindowcalories);

        back = (Button) findViewById(R.id.buttonClosePop);
        goalInput = (EditText) findViewById(R.id.goalnput);
        viewGoal = (TextView) findViewById(R.id.viewGoal);
        viewIdeal = (TextView) findViewById(R.id.viewIdeal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        userLocalStore = new UserLocalStore(this);
        username = userLocalStore.getUserLoggedIn();
        user = userLocalStore.getUserInfo(username);

        setViewIdeal();
        viewGoal.setText("Your daily goal is set to " + user.caloriesGoal + " kcal");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopUp();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void changeGoal(View v) {
        if (goalInput.getText().toString().trim().length() > 0) {
            goal = Integer.valueOf(goalInput.getText().toString());
            if (goal > 9999) {
                Toast.makeText(getApplicationContext(), "Goal is above limit!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                viewGoal.setText("Your daily goal is set to " + goal + " kcal");

                userLocalStore.getUserInfo(username);
                User changedUser = new User(user.firstName, user.lastName, user.username, user.password,
                        user.salt, user.sex, user.dateOfBirth, user.age, user.height, user.weight, goal, user.sleepGoal);
                userLocalStore.storeUserData(changedUser);
            }
        } else {
            Toast.makeText(this, "Please enter an amount first", Toast.LENGTH_SHORT).show();
        }
    }

    public void setViewIdeal() {
        ideal = user.idealCalories;
        viewIdeal.setText("Your recommended daily calorie intake is " + ideal + " kcal");
    }

    public void closePopUp() { finish(); }

    @Override
    public void onBackPressed() { closePopUp(); }

}
