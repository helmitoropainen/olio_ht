package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;



public class SleepActivity extends AppCompatActivity {

    Button returnHome;
    EditText hour1, minute1, hour2, minute2;
    TextView sum, readinesstext, advicetext ;
    int h1=0, m1=0, h2=0, m2=0, slepth=0, sleptmin=0, mindifference=0, readiness=0, goal=8;
    double slepttime=0;
    boolean hasUerCalculatedSleep = false;
    String username, date;
    sleepEntry SE = new sleepEntry(0,0,0,0);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        setTitle(R.string.app_name);

        returnHome = (Button) findViewById(R.id.returnHome);

        hour1 = (EditText) findViewById(R.id.editTextHour1) ;
        minute1 = (EditText) findViewById(R.id.editTextMinute1) ;
        hour2 = (EditText) findViewById(R.id.editTextHour2) ;
        minute2 = (EditText) findViewById(R.id.editTextMinute2) ;
        sum = (TextView) findViewById(R.id.textViewSum) ;
        readinesstext = (TextView) findViewById(R.id.textViewReadiness);
        advicetext = (TextView) findViewById(R.id.textViewComment) ;

        // jos h1<00.00, h1:n päivä on h2:n päivä -1. Jos h1>00.00, h1:n päivä = h2:n päivä

        username = "user";
        date = "24.4.2021";

        SE.setDate(date);
        SE.setUsername(username);

        returnHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("sleep entry", SE);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void submitTime(View v) {

        if (hour1.getText().toString().matches("") || hour2.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter required times", Toast.LENGTH_SHORT).show();
            return;
        }
        if (minute1.getText().toString().matches("")) {
            minute1.setText("00");
        } if (minute2.getText().toString().matches("")) {
            minute2.setText("00");
        }

        h1 = Integer.parseInt(hour1.getText().toString());
        m1 = Integer.parseInt(minute1.getText().toString());
        h2 = Integer.parseInt(hour2.getText().toString());
        m2 = Integer.parseInt(minute2.getText().toString());

        if (h1>23 || m1>59 || h2>23 || m2>59) {
            Toast.makeText(this, "Invalid numbers given for time", Toast.LENGTH_SHORT).show();
            return;
        }

        SE = new sleepEntry(h1, h2, m1, m2) ;
        mindifference = SE.calculateTime();

        String sumText = SE.getHoursAndMinsText(mindifference) ;
        sum.setText(sumText);

        readiness = SE.getReadiness() ;
        readinesstext.setText("You've reached "+readiness+"% of your goal") ;

        String advice = SE.getAdvice(readiness) ;
        advicetext.setText(advice);

        slepttime = SE.getSleptTime();
        SE.setDate(date);
        SE.setUsername(username);
        SE.setSum(slepttime);

        hasUerCalculatedSleep = true;

    }

    public void resetData(View v) {
        if (hasUerCalculatedSleep == false) {
            SE = new sleepEntry(0,0,0,0);
            hasUerCalculatedSleep = true;
        }
        SE.resetTodaysSum();
    }

    public void sleepRecommendation (View v) {
        startActivity(new Intent(SleepActivity.this, PopUpSleep.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (hasUerCalculatedSleep == true) {
            intent.putExtra("sleep entry", SE);
            setResult(RESULT_OK, intent);
        } else {
            intent.putExtra("no data", SE);
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }
}
