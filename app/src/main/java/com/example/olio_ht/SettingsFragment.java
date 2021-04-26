package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;




public class SettingsFragment extends Fragment {

    TextView dateView;
    View view;
    Spinner spinner;
    UserLocalStore uls;
    Button btLogOut;
    Button btApplyChanges;
    TextView tvUserName;
    EditText etFirstName;
    EditText etLastName;
    EditText etHeight;
    EditText etWeight;
    TextView tvBMI;
    TextView tvInfoBMI;
    int choice;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinner = view.findViewById(R.id.sexSpinner);
        uls = new UserLocalStore(getActivity().getBaseContext());
        tvUserName = view.findViewById(R.id.textView14);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        dateView = view.findViewById(R.id.dateOfBirth);
        btLogOut = view.findViewById(R.id.button3);
        tvBMI = view.findViewById(R.id.idBMI);
        tvInfoBMI = view.findViewById(R.id.tvInfoBMI);

        String username = uls.getUserLoggedIn();
        tvUserName.setText(username);
        etFirstName.setText(uls.getUserInfo(username).firstName);
        etLastName.setText(uls.getUserInfo(username).lastName);
        etHeight.setText(String.valueOf(Math.round(uls.getUserInfo(username).height)));
        etWeight.setText(String.valueOf(Math.round(uls.getUserInfo(username).weight)));
        tvBMI.setText(String.valueOf(Math.round(uls.getUserInfo(username).bmi)));
        String bmiInfo = getBmiInfo(uls.getUserInfo(username).bmi);
        tvInfoBMI.setText(bmiInfo);

        LocalDate dateOfBirth = uls.getUserInfo(username).dateOfBirth;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = dateOfBirth.format(formatter);
        dateView.setText(formattedDate);


        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        btApplyChanges = view.findViewById(R.id.button);
        btApplyChanges.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sexes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int sex = getSpinnerPosition(uls.getUserInfo(username).sex);
        spinner.setSelection(sex);
    }

    public int getSpinnerPosition(String sex) {
        int i;
        Resources res = getResources();
        String[] sexes = res.getStringArray(R.array.sexes);
        for (i = 0; i < sexes.length; i++) {
            if (sex.equals(sexes[i])) {
                break;
            }
        }
        return i;
    }

    public void changeDate() {
        String arg = getArguments().getString("date");
        dateView = (TextView) this.view.findViewById(R.id.dateOfBirth);
        dateView.setText(arg);
    }

    public void logOut() {
        uls.setUserLoggedIO("");
        Intent intent = new Intent(getActivity().getBaseContext(), LogInActivity.class);
        startActivityForResult(intent, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void applyChanges() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate now = LocalDate.now();
        String username = uls.getUserLoggedIn();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        LocalDate dateOfBirth = LocalDate.parse(dateView.getText(), formatter);
        int age = (int) java.time.temporal.ChronoUnit.YEARS.between(dateOfBirth, now);
        String height = etHeight.getText().toString();
        String weight = etWeight.getText().toString();
        String sex = spinner.getItemAtPosition(choice).toString();
        String securePassword = uls.getUserInfo(username).password;
        String salt = uls.getUserInfo(username).salt;
        long sleepGoal = uls.getUserInfo(username).sleepGoal;
        long caloriesGoal = uls.getUserInfo(username).caloriesGoal;

        if (firstName.isEmpty()) {
            etFirstName.setError("Field can't be empty!");
            etFirstName.requestFocus();
            return;
        } if (lastName.isEmpty()) {
            etLastName.setError("Field can't be empty!");
            etLastName.requestFocus();
            return;
        } if (dateOfBirth.toString().isEmpty()) {
            dateView.setError("Field can't be empty!");
            dateView.requestFocus();
            return;
        } if (height.isEmpty()) {
            etHeight.setError("Field can't be empty!");
            etHeight.requestFocus();
            return;
        } if (weight.isEmpty()) {
            etWeight.setError("Field can't be empty!");
            etWeight.requestFocus();
            return;
        }

        User changedUser = new User(firstName, lastName, username, securePassword, salt, sex,
                dateOfBirth, age, parseFloat(height), parseFloat(weight), caloriesGoal, sleepGoal);
        uls.storeUserData(changedUser);

        System.out.println(uls.getUserInfo(username).bmi);
        tvBMI.setText(String.valueOf(Math.round(uls.getUserInfo(username).bmi)));
        String bmiInfo = getBmiInfo(uls.getUserInfo(username).bmi);
        tvInfoBMI.setText(bmiInfo);

        Context context = getContext();
        CharSequence text = "User data changed successfully.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public String getBmiInfo(float bmi) {
        String bmiInfo = "";
        if (bmi < 18.5) {
            bmiInfo = "Underweight";
        } else if (bmi >= 18.5 && bmi < 25) {
            bmiInfo = "Normal weight";
        } else if (bmi >= 25 && bmi < 30) {
            bmiInfo = "Overweight";
        } else if (bmi >= 30) {
            bmiInfo = "Obese";
        }
        return bmiInfo;
    }
}

