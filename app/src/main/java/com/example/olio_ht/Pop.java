package com.example.olio_ht;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Pop extends Activity {

    EditText goalInput;
    TextView viewGoal;
    int goal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        goalInput = (EditText) findViewById(R.id.goalnput);
        viewGoal = (TextView) findViewById(R.id.viewGoal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }

    public void changeGoal(View v) {
        if (goalInput.getText().toString().trim().length() > 0) {
            goal = Integer.valueOf(goalInput.getText().toString());
            if (goal > 9999) {
                Toast.makeText(getApplicationContext(), "Goal is above limit!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                viewGoal.setText("Your daily goal is set to " + goal + " kcal");
            }
        }
    }

    public void closePopUp(View v) {
        finish();
    }
}
