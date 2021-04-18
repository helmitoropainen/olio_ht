package com.example.olio_ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;

public class CalorieActivity extends AppCompatActivity {

    Button returnHome;
    ArrayList<String> sports_array = new ArrayList<String>();
    ArrayList<String> food_array = new ArrayList<String>();
    RecyclerView sportsRecyclerView;
    RecyclerView foodRecyclerView;
    Spinner sportsSpinner;
    Spinner foodSpinner;
    SeekBar durationSeekBar;
    SeekBar massSeekBar;
    TextView viewDuration;
    TextView viewMass;
    int duration, mass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        returnHome = (Button) findViewById(R.id.returnHome);
        sportsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_sports);
        foodRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_food);
        sportsSpinner = (Spinner) findViewById(R.id.sportsSpinner);
        foodSpinner = (Spinner) findViewById(R.id.foodSpinner);
        durationSeekBar = (SeekBar) findViewById(R.id.seekBarD);
        massSeekBar = (SeekBar) findViewById(R.id.seekBarM);
        viewDuration = (TextView) findViewById(R.id.durationView);
        viewMass = (TextView) findViewById(R.id.massView);

        Collections.addAll(sports_array, getResources().getStringArray(R.array.sports));
        Collections.addAll(food_array, getResources().getStringArray(R.array.food));

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.sports, android.R.layout.simple_spinner_item);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapterS);
        sportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapterF = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_item);
        adapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(adapterF);
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = (int) progress*10;
                viewDuration.setText(duration + " min");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        massSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mass = (int) progress*10;
                viewMass.setText(mass + " g");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        initRecyclerView(sportsRecyclerView, sports_array);
        initRecyclerView(foodRecyclerView, food_array);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void initRecyclerView(RecyclerView rv, ArrayList array) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(array, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}