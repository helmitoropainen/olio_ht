package com.example.olio_ht;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class PopUpSleep extends Activity {

    EditText goalInputH, goalInputMin;
    TextView viewGoal;
    int goalh, goalmin;
    double goal ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindowsleep);

        goalInputH = (EditText) findViewById(R.id.goalinputh);
        goalInputMin = (EditText) findViewById(R.id.goalinputmin);
        viewGoal = (TextView) findViewById(R.id.viewGoal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }

    public void changeGoal(View v) {
        if (goalInputH.getText().toString().trim().length() > 0) {
            goalh = Integer.parseInt(goalInputH.getText().toString());
            goalmin = Integer.parseInt(goalInputMin.getText().toString());
            if (goalh > 23 || goalmin > 59) {
                Toast.makeText(getApplicationContext(), "Goal is above limit!", Toast.LENGTH_SHORT).show();
            } else {
                if (goalmin == 0) {
                    viewGoal.setText("Your daily goal is set to " + goalh + " hours");
                    goal = (double) goalh ;
                } else {
                    viewGoal.setText("Your daily goal is set to\n" + goalh + " hours and " + goalmin + " minutes");
                    goal = (double) goalh + goalmin/60 ;
                }
            }
        }
    }

    public void closePopUp(View v) {
        finish();
    }
}
