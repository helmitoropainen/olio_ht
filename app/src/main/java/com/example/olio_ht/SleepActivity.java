package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepActivity extends AppCompatActivity {

    Button returnHome, submit;
    EditText hour1, minute1, hour2, minute2;
    TextView sum, readinesstext, commenttext ;
    int h1=0, m1=0, h2=0, m2=0, slepth=0, sleptmin=0, mindifference=0, readiness=0, goal=8;
    float slepttime=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        returnHome = (Button) findViewById(R.id.returnHome);

        hour1 = (EditText) findViewById(R.id.editTextHour1) ;
        minute1 = (EditText) findViewById(R.id.editTextMinute1) ;
        hour2 = (EditText) findViewById(R.id.editTextHour2) ;
        minute2 = (EditText) findViewById(R.id.editTextMinute2) ;
        sum = (TextView) findViewById(R.id.textViewSum) ;
        readinesstext = (TextView) findViewById(R.id.textViewReadiness);
        commenttext = (TextView) findViewById(R.id.textViewComment) ;

        // jos h1<00.00, h1:n päivä on h2:n päivä -1. Jos h1>00.00, h1:n päivä = h2:n päivä

        returnHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
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
        } else if (h1>12) {
            m1 = 24*60 - 60*h1 - m1 ;
            if (h2>h1) {
                m2 = 24*60 - h2*60 - m2 ;
                mindifference = m2 - m1 ;
            } else {
                m2 = h2*60 + m2 ;
                mindifference = m2 + m1 ;
            }
        } else if (h1<12) {
            m1 = 60*h1 + m1 ;
            m2 = h2*60 + m2 ;
            mindifference = m2 - m1 ;
        }

        slepth = (int) mindifference/60 ;
        sleptmin = mindifference - slepth*60 ;
        slepttime = (float) mindifference/60;

        if (sleptmin == 0) {
            sum.setText("You slept\n"+slepth+" hours") ;
        } else if (slepth == 0) {
            sum.setText("You slept\n"+sleptmin+" hours") ;
        }
        else {
            sum.setText("You slept\n" + slepth + " hours and " + sleptmin + " minutes");
        }

        readiness = (int) ((slepttime/goal) * 100) ;

        readinesstext.setText("You've reached "+readiness+"% of your goal");

        if (readiness<30) {
            commenttext.setText("Take it easy on yourself today and try to sleep early!") ;
        } else if (readiness<50) {
            commenttext.setText("Remember to stay hydrated and eat nutritious food. You can get through this day!") ;
        } else if (readiness<75) {
            commenttext.setText("A bit of exercising could help you feel more awake!") ;
        } else if (readiness<95) {
            commenttext.setText("You're off to a good start today, just remember to take consistent breaks from your work!") ;
        } else if (readiness<100) {
            commenttext.setText("Alright! You're getting there!") ;
        } else if (readiness<115) {
            commenttext.setText("Yay, you've reached your goal! Now you can go out there and conquer this day!") ;
        } else {
            commenttext.setText("Remember that too much sleep can increase your tiredness.") ;
        }

    }

    public void resetData(View v) {

    }

    public void sendSleptTimeToHome (View view) {
        // tässä koitin lähettää sleptTime arvon homefragmenttiin, muttei onnistunut :)
        Bundle bundle = new Bundle() ;
        bundle.putFloat("sleptTime", slepttime);
        HomeFragment home = new HomeFragment();
        home.setArguments(bundle);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
