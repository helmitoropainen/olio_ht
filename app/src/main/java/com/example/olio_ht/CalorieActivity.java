package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.round;

public class CalorieActivity extends AppCompatActivity implements RecyclerViewAdapter.OnTextClickListener, Filterable {

    Button returnHome;
    ArrayList<SportData> sports_array = new ArrayList<SportData>();
    ArrayList<SportData> sports_array_full = new ArrayList<SportData>();
    ArrayList<FoodData> food_array = new ArrayList<FoodData>();
    ArrayList<CalorieEntry> spent_array;
    ArrayList<CalorieEntry> gained_array;
    ArrayAdapter<ArrayList> adapterS;
    RecyclerView sportsRecyclerView, foodRecyclerView;
    Spinner sportsSpinner, foodSpinner;
    SeekBar durationSeekBar, massSeekBar;
    SearchView searchSport;
    SearchView searchFood;
    TextView viewDuration, viewMass, viewSpentCalories, viewGainedCalories, sumView;
    EditText caloriesSpentInput, calorieIntakeInput;
    String sportType, foodType, className, sportName, foodName, username, date;
    int duration, mass, i, weight;
    long spentCalories, gainedCalories, sum;
    double foodCalories, sportCalories;
    FoodEntry FE;
    SportEntry SE;
    FoodData FD;
    SportData SD;
    Context context;
    SharedPreferences sharedPreferences;

    EntryManager entryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        context = CalorieActivity.this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        readJSON();
        this.sports_array = readFile();
        sports_array_full = new ArrayList<SportData>(sports_array);

        weight = 70;
        username = "user";
        date = "24.4.2021";

        FE = new FoodEntry(foodType, mass, gainedCalories);
        SE = new SportEntry(foodType, mass, gainedCalories);

        FE.setDate(date);
        FE.setUsername(username);
        SE.setDate(date);
        SE.setUsername(username);

        returnHome = (Button) findViewById(R.id.returnHome);
        searchSport = (SearchView) findViewById(R.id.searchViewSport);
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

        entryManager = EntryManager.getInstance();

        loadArrays();
        spent_array = initRecyclerView(sportsRecyclerView, spent_array);
        gained_array = initRecyclerView(foodRecyclerView, gained_array);
        countSum();

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveArrays();
                Intent intent = new Intent();
                // kesken intent.putExtra("todays sum", sum);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        adapterS = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sports_array);
        adapterS.setNotifyOnChange(true);
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapterS);
        sportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sportType = sports_array.get(position).getSportName();
                SD = sports_array.get(position);
                SE.setSportType(sportType);
                if (sportType.equals("Own workout") == false) {
                    caloriesSpentInput.setEnabled(false);
                    durationSeekBar.setEnabled(true);
                    duration = (int) durationSeekBar.getProgress() * 10;
                } else {
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
                    SE.setCalories(spentCalories);
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
                foodType = food_array.get(position).getFoodName();
                FD = food_array.get(position);
                FE.setFoodType(foodType);
                if (foodType.equals("Own portion") == false) {
                    calorieIntakeInput.setEnabled(false);
                    massSeekBar.setEnabled(true);
                    mass = (int) massSeekBar.getProgress() * 10;
                } else {
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
                    FE.setCalories(gainedCalories);
                }
                FE.setFoodAmount(mass);
                gainedCalories = round(FE.countGainedCalories(FE, FD));
                displayGainedCalories(gainedCalories);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchSport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapterS.getFilter().filter(newText);
                return false;
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
                SE.setCalories(spentCalories);
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
                FE.setCalories(gainedCalories);
                displayGainedCalories(gainedCalories);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveArrays();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public class CustomFilter extends Filter {
        ArrayList<SportData> arrayList;
        ArrayAdapter adapter;
        ArrayList<SportData> filterArrayList;
        public  CustomFilter (ArrayList<SportData> arrayList, ArrayAdapter adapter) {
            this.arrayList = arrayList;
            this.adapter = adapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SportData> filteredList = new ArrayList<SportData>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sports_array_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SportData item : sports_array_full) {
                    if (item.getSportName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sports_array.clear();
            sports_array.addAll((ArrayList) results.values);
            adapterS.clear();
            adapterS.add(sports_array);
            sports_array = updateArray(sports_array);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SportData> filteredList = new ArrayList<SportData>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sports_array_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SportData item : sports_array_full) {
                    if (item.getSportName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sports_array.clear();
            sports_array.addAll((ArrayList) results.values);
            adapterS.clear();
            adapterS.add(sports_array);
            sports_array = updateArray(sports_array);
        }
    };

    public ArrayList<SportData> updateArray(ArrayList<SportData> array) {
        return  array;
    }

    public ArrayList initRecyclerView(RecyclerView rv, ArrayList array) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(array, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        //array.clear();
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
        countSum();
        SE = new SportEntry(sportType, duration, spentCalories);
        SE.setDate(date);
        SE.setUsername(username);
    }

    public void addGainedCalories(View v) {
        gained_array.add(FE);
        gained_array = initRecyclerView(foodRecyclerView, gained_array);
        countSum();
        FE = new FoodEntry(foodType, mass, gainedCalories);
        FE.setDate(date);
        FE.setUsername(username);
    }

    public void resetSport(View v) {
        spent_array.clear();
        spent_array = initRecyclerView(sportsRecyclerView, spent_array);
        countSum();
    }

    public void resetFood(View v) {
        int i;
        gained_array.clear();
        countSum();
        initRecyclerView(foodRecyclerView, gained_array);
    }

    public void countSum() {
        double add = 0, sub = 0;
        sum = 0;
        for (i = 0; i < gained_array.size(); i++) {
            add = add + gained_array.get(i).getCalories();
        }
        for(i=0; i<spent_array.size(); i++) {
            sub = sub + spent_array.get(i).getCalories();
        }
        sum = round(add - sub);

        // täs vaa testailen tot
        SE.setSum(sub);
        FE.setSum(add);

        entryManager.setSportEntries(SE);
        entryManager.setFoodEntries(FE);

        sumView.setText("Todays calories are " + sum + " kcal!");
    }

    public void calorieRecommendation (View v) {
        startActivity(new Intent(CalorieActivity.this, Pop.class));
    }

    @Override
    public void onTextClick(ArrayList<CalorieEntry> array) {
        if (className.equals("sportEntry") == true) {
            spent_array = array;
        } else if (className.equals("foodEntry") == true) {
            gained_array = array;
        }
    }

    @Override
    public void getClass(String cN) {
        className = cN;
    }

    public void readJSON () {
        String json = getJSON();

        if (json != null) {
            try {
                FD = new FoodData();
                FD.setFoodName("Own portion");
                FD.setCalories(0);
                food_array.add(FD);
                JSONArray jsonArray = new JSONArray(json);
                for (i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FD = new FoodData();
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

    public ArrayList<SportData> readFile() {
        try {
            InputStream ins = getAssets().open("exercise_data.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String s = "";
            br.readLine();
            SD = new SportData();
            SD.setSportName("Own workout");
            SD.setCalories(0);
            sports_array.add(SD);
            while ((s = br.readLine()) != null) {
                String[] tokens = s.split(",");
                sportName = tokens[0];
                int p = (int) tokens.length - 1;
                sportCalories = Double.valueOf(tokens[p]);
                SD = new SportData();
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
        return sports_array;
    }

    public void saveArrays() {
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsonSe = new Gson();
        Gson gsonFe = new Gson();
        String jsonSe = gsonSe.toJson(spent_array);
        String jsonFe = gsonFe.toJson(gained_array);
        editor.putString("spent array", jsonSe);
        editor.putString("gained array", jsonFe);
        editor.apply();
    }

    private void loadArrays() {
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gsonSe = new Gson();
        Gson gsonFe = new Gson();
        String jsonSe = sharedPreferences.getString("spent array", null);
        String jsonFe = sharedPreferences.getString("gained array", null);
        Type type = new TypeToken<ArrayList<CalorieEntry>>() {}.getType();

        spent_array = gsonSe.fromJson(jsonSe, type);
        gained_array = gsonFe.fromJson(jsonFe, type);

        if (spent_array == null) {
            spent_array = new ArrayList<CalorieEntry>();
        }
        if (gained_array == null) {
            gained_array = new ArrayList<CalorieEntry>();
        }
    }
}