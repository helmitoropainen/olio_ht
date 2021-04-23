package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.round;

public class CalorieActivity extends AppCompatActivity implements RecyclerViewAdapter.OnTextClickListener {

    Button returnHome;
    ArrayList<sportData> sports_array = new ArrayList<sportData>();
    ArrayList<foodData> food_array = new ArrayList<foodData>();
    ArrayList<calorieEntry> spent_array = new ArrayList<calorieEntry>();
    ArrayList<calorieEntry> gained_array = new ArrayList<calorieEntry>();
    RecyclerView sportsRecyclerView, foodRecyclerView;
    Spinner sportsSpinner, foodSpinner;
    SeekBar durationSeekBar, massSeekBar;
    TextView viewDuration, viewMass, viewSpentCalories, viewGainedCalories, sumView;
    EditText caloriesSpentInput, calorieIntakeInput;
    String sportType, foodType, className, sportName, foodName;
    int duration, mass, i, weight;
    long spentCalories, gainedCalories;
    double foodCalories, sportCalories;
    foodEntry FE;
    sportEntry SE;
    foodData FD;
    sportData SD;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        context = CalorieActivity.this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        readJSON();
        readFile();

        FE = new foodEntry(foodType, mass, gainedCalories);
        SE = new sportEntry(foodType, mass, gainedCalories);

        returnHome = (Button) findViewById(R.id.returnHome);
        sportsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_sports);
        foodRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_food);
        sportsSpinner = (Spinner) findViewById(R.id.sportsSpinner);
        foodSpinner = (Spinner) findViewById(R.id.foodSpinner);
        durationSeekBar = (SeekBar) findViewById(R.id.seekBarD);
        massSeekBar = (SeekBar) findViewById(R.id.seekBarM);
        viewDuration = (TextView) findViewById(R.id.durationView);
        viewMass = (TextView) findViewById(R.id.massView);
        viewSpentCalories = (TextView) findViewById(R.id.spentCalories);
        viewGainedCalories = (TextView) findViewById(R.id.gainedCalories);
        caloriesSpentInput = (EditText) findViewById(R.id.caloriesSpentInput);
        calorieIntakeInput = (EditText) findViewById(R.id.calorieIntakeInput);
        sumView = (TextView) findViewById(R.id.sumView);

        weight = 70;

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ArrayAdapter<CharSequence> adapterS = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sports_array);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapterS);
        sportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    sportType = sports_array.get(position).getSportName();
                    SD = sports_array.get(position);
                    SE.setSportType(sportType);
                    caloriesSpentInput.setEnabled(false);
                    durationSeekBar.setEnabled(true);
                    duration = (int) durationSeekBar.getProgress() * 10;
                } else {
                    sportType = sports_array.get(position).getSportName();
                    SE.setSportType(sportType);
                    SD = sports_array.get(position);
                    caloriesSpentInput.setEnabled(true);
                    durationSeekBar.setEnabled(false);
                    duration = 0;
                    if (caloriesSpentInput.getText().toString().trim().length() > 0) {
                        try {
                            spentCalories = Integer.valueOf(caloriesSpentInput.getText().toString().trim());
                        } catch(Exception ex) {
                            spentCalories = 0;
                            caloriesSpentInput.setText("");
                        }
                    } else {
                        spentCalories = 0;
                    }
                    SE.setSpentCalories(spentCalories);
                }
                SE.setDuration(duration);
                spentCalories = round(SE.countSpentCalories(SE, SD, weight));
                displaySpentCalories(spentCalories);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapterF = new ArrayAdapter(this, android.R.layout.simple_spinner_item, food_array);
        adapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(adapterF);
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    foodType = food_array.get(position).getFoodName();
                    FD = food_array.get(position);
                    FE.setFoodType(foodType);
                    calorieIntakeInput.setEnabled(false);
                    massSeekBar.setEnabled(true);
                    mass = (int) massSeekBar.getProgress() * 10;
                } else {
                    foodType = food_array.get(position).getFoodName();
                    FD = food_array.get(position);
                    FE.setFoodType(foodType);
                    calorieIntakeInput.setEnabled(true);
                    massSeekBar.setEnabled(false);
                    mass = 0;
                    if (calorieIntakeInput.getText().toString().trim().length() > 0) {
                        try {
                            gainedCalories = Integer.valueOf(caloriesSpentInput.getText().toString().trim());
                        } catch(Exception ex) {
                            gainedCalories = 0;
                            calorieIntakeInput.setText("");
                        }
                    } else {
                        gainedCalories = 0;
                    }
                    FE.setFoodCalories(gainedCalories);
                }
                FE.setFoodAmount(mass);
                gainedCalories = round(FE.countGainedCalories(FE, FD));
                displayGainedCalories(gainedCalories);
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
                SE.setDuration(duration);
                spentCalories = round(SE.countSpentCalories(SE, SD, weight));
                displaySpentCalories(spentCalories);
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
                FE.setFoodAmount(mass);
                gainedCalories = round(FE.countGainedCalories(FE, FD));
                displayGainedCalories(gainedCalories);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        caloriesSpentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    spentCalories = Integer.valueOf(s.toString().trim());
                } else {
                    spentCalories = 0;
                }
                SE.setSpentCalories(spentCalories);
                displaySpentCalories(spentCalories);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        calorieIntakeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    gainedCalories = Integer.valueOf(s.toString().trim());
                } else {
                    gainedCalories = 0;
                }
                FE.setFoodCalories(gainedCalories);
                displayGainedCalories(gainedCalories);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public ArrayList initRecyclerView(RecyclerView rv, ArrayList array) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(array, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        array = adapter.getArray();
        return array;
    }

    public void displayGainedCalories(long gc) {
        viewGainedCalories.setText("You gained " + gc + " kcal");
    }

    public void displaySpentCalories(long sc) {
        viewSpentCalories.setText("You spent " + sc + " kcal");
    }

    public void addSpentCalories(View v) {
        spent_array.add(SE);
        spent_array = initRecyclerView(sportsRecyclerView, spent_array);
        countSum(gained_array, spent_array);
        SE = new sportEntry(sportType, duration, spentCalories);
    }

    public void addGainedCalories(View v) {
        gained_array.add(FE);
        gained_array = initRecyclerView(foodRecyclerView, gained_array);
        countSum(gained_array, spent_array);
        FE = new foodEntry(foodType, mass, gainedCalories);
    }

    public void resetSport(View v) {
        spent_array.clear();
        spent_array = initRecyclerView(sportsRecyclerView, spent_array);
        countSum(gained_array, spent_array);
    }

    public void resetFood(View v) {
        int i;
        gained_array.clear();
        countSum(gained_array, spent_array);
        initRecyclerView(foodRecyclerView, gained_array);
    }

    public void countSum(ArrayList<calorieEntry> gained, ArrayList<calorieEntry> spent) {
        double add = 0, sub = 0;
        long sum = 0;
        for (i = 0; i < gained.size(); i++) {
            add = add + gained.get(i).getCalories();
        }
        for(i=0; i<spent.size(); i++) {
            sub = sub + spent.get(i).getCalories();
        }
        sum = round(add - sub);
        sumView.setText("Todays calories are " + sum + " kcal!");
    }

    public void calorieRecommendation (View v) {
        startActivity(new Intent(CalorieActivity.this, Pop.class));
    }

    @Override
    public void onTextClick(ArrayList<calorieEntry> array) {
        if (className.equals("sportEntry") == true) {
            countSum(gained_array, array);
        } else if (className.equals("foodEntry") == true) {
            countSum(array, spent_array);
        }

    }

    @Override
    public void getClass(String cN) {
        className = cN;
        System.out.println(className);
    }

    public void readJSON () {
        String json = getJSON();
        //System.out.println("JSON:" + json);

        if (json != null) {
            try {
                FD = new foodData();
                FD.setFoodName("Own portion");
                FD.setCalories(0);
                food_array.add(FD);
                JSONArray jsonArray = new JSONArray(json);
                for (i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FD = new foodData();
                    foodName = jsonObject.getJSONObject("name").get("en").toString();
                    foodCalories = Double.valueOf(jsonObject.get("energyKcal").toString());
                    FD.setCalories(foodCalories);
                    FD.setFoodName(foodName);
                    food_array.add(FD);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getJSON() {
        String response = null;
        try {
            URL url = new URL("https://fineli.fi/fineli/api/v1/foods");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void readFile() {
        try {
            InputStream ins = getAssets().open("exercise_data.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String s = "";
            br.readLine();
            SD = new sportData();
            SD.setSportName("Own workout");
            SD.setCalories(0);
            sports_array.add(SD);
            while ((s = br.readLine()) != null) {
                String[] tokens = s.split(",");
                sportName = tokens[0];
                int p = (int) tokens.length - 1;
                sportCalories = Double.valueOf(tokens[p]);
                SD = new sportData();
                SD.setCalories(sportCalories);
                SD.setSportName(sportName);

                int l = (int) sports_array.size() - 1;
                    if (sports_array.get(l).getSportName().equals(sportName) == false) {
                        sports_array.add(SD);
                    }
                }

        } catch (FileNotFoundException e) {
            Log.e("FileNotFound", "File not found");
        } catch (IOException e) {
            Log.e("IOException", "Error in input");
        }
    }
}