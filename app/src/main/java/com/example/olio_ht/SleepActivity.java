package com.example.olio_ht;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SleepActivity extends AppCompatActivity {

    Button returnHome;
    EditText hour1, minute1, hour2, minute2;
    TextView sum, readinesstext, advicetext, goalView;
    int h1=0, m1=0, h2=0, m2=0, slepth=0, sleptmin=0, mindifference=0, readiness=0, goal=8;
    double slepttime=0, goalh = 0;
    String username, date;
    SleepEntry SE = new SleepEntry(0,0,0,0);
    User user;
    SharedPreferences sharedPreferences;
    UserLocalStore userLocalStore;

    // HELMI
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        setTitle(R.string.app_name);

        userLocalStore = new UserLocalStore(this);
        username = userLocalStore.getUserLoggedIn();
        user = userLocalStore.getUserInfo(username);

        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        date = dateNow.format(formatter);

        SE.setDate(date);
        SE.setUsername(username);
        SE.setGoal(user.sleepGoal);

        returnHome = (Button) findViewById(R.id.returnHome);
        hour1 = (EditText) findViewById(R.id.editTextHour1) ;
        minute1 = (EditText) findViewById(R.id.editTextMinute1) ;
        hour2 = (EditText) findViewById(R.id.editTextHour2) ;
        minute2 = (EditText) findViewById(R.id.editTextMinute2) ;
        sum = (TextView) findViewById(R.id.textViewSum) ;
        readinesstext = (TextView) findViewById(R.id.textViewReadiness);
        advicetext = (TextView) findViewById(R.id.textViewComment) ;
        goalView = (TextView) findViewById(R.id.goalView);

        loadState();
        goalh = (double) user.sleepGoal/60;
        goalView.setText(String.format("Your nightly sleep goal: %.2f hours", goalh));
        String sumText = SE.getHoursAndMinsText(mindifference) ;
        sum.setText(sumText);
        readinesstext.setText("You've reached "+readiness+"% of your goal") ;
        String advice = SE.getAdvice(readiness) ;
        advicetext.setText(advice);

        // When return home button is clicked, the calculated slept time is saved, and the user is
        // redirected to the home fragment.
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveState();
                SE.setSum(slepttime);
                Intent intent = new Intent();
                intent.putExtra("sleep entry", SE);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // This method handles the submission of the given times. If inputs are lacking or faulty, the
    // user is notified of this or other actions are taken. Acceptable inputs are saved in a
    // SleepEntry object.
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

        SE = new SleepEntry(h1, h2, m1, m2) ;
        SE.setDate(date);
        SE.setUsername(username);
        SE.setGoal(user.sleepGoal);
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
    }

    // Today's data is reset to zero.
    public void resetData(View v) {
        SE.setSum(0);
        readiness = 0;
        mindifference = 0;
        slepttime = 0;
        String sumText = SE.getHoursAndMinsText(mindifference) ;
        sum.setText(sumText);
        readinesstext.setText("You've reached "+readiness+"% of your goal") ;
        advicetext.setText("");
        saveState();
    }

    // HELMI
    public void sleepRecommendation (View v) {
        startActivityForResult(new Intent(SleepActivity.this, com.example.olio_ht.PopUpSleep.class), 1);
    }

    // HELMI
    @Override
    public void onBackPressed() {
        saveState();
        SE.setSum(slepttime);
        Intent intent = new Intent();
        intent.putExtra("sleep entry", SE);
        setResult(RESULT_OK, intent);
        finish();
    }

    //  HELMI
    public void saveState() {
        String spName = "shared preferences" + username;
        sharedPreferences = getSharedPreferences(spName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putFloat("slept time", (float) slepttime);
        editor.putInt("min dif", mindifference);
        editor.putInt("readiness", readiness);
        editor.apply();
    }

    // HELMI
    private void loadState() {
        String spName = "shared preferences" + username;
        sharedPreferences = getSharedPreferences(spName, MODE_PRIVATE);

        slepttime = (double) sharedPreferences.getFloat("slept time", 0);
        mindifference = sharedPreferences.getInt("min dif", 0);
        readiness = sharedPreferences.getInt("readiness", 0);

    }

    // HELMI
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                user = userLocalStore.getUserInfo(username);
                goalh = (double) user.sleepGoal / 60;
                goalView.setText(String.format("Your nightly sleep goal: %.2f hours", goalh));
            }
        }
    }
}
