package com.example.olio_ht;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    TextView dateView;
    View view;
    Spinner spinner;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String BIRTHDAY = "birthday";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String SEX = "sex";

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinner = view.findViewById(R.id.sexSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sexes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void changeDate() {
        String arg = getArguments().getString("date");
        dateView = (TextView) this.view.findViewById(R.id.dateOfBirth);
        dateView.setText(arg);
    }

    // näist en oo viel ihan varma, tämmösii käytin 11? viikol ku halusin et asetukset säilyy vaik
    // relaunchaa activityn, tyylii appin voi myös sulkee ja säilyy silti.. mut en tiiä ku meil on
    // ne käyttäjät et meneeks eri taval
  /*  public void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, usernameInput.getText().toString());
        editor.putInt(FONT_SIZE, fontSize);
        editor.putBoolean(EDIT_SWITCH, editValue);
        editor.putBoolean(BOLD_SWITCH, boldValue);
        editor.putBoolean(ITALIC_SWITCH, italicValue);
        editor.putInt(SPINNER_STATE, spinnerPosition);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME, "");
        editValue = sharedPreferences.getBoolean(EDIT_SWITCH, false);
        boldValue = sharedPreferences.getBoolean(BOLD_SWITCH, false);
        italicValue = sharedPreferences.getBoolean(ITALIC_SWITCH, false);
        fontSize = sharedPreferences.getInt(FONT_SIZE, 10);
        spinnerPosition = sharedPreferences.getInt(SPINNER_STATE, 0);
    }

    public void updateViews() {
        editSwitch.setChecked(editValue);
        boldSwitch.setChecked(boldValue);
        italicSwitch.setChecked(italicValue);
        seekBar.setProgress((fontSize-10)/2);
        usernameInput.setText(username);
        spinner.setSelection(spinnerPosition);
    }*/
}

