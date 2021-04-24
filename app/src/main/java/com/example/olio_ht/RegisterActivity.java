package com.example.olio_ht;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static java.lang.Float.parseFloat;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etFirstName;
    EditText etLastName;
    TextView tvDateOfBirth;
    EditText etHeight;
    EditText etWeight;
    Spinner spinner;
    EditText etPassword;
    EditText etConfirmPassword;
    int choice;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etUsername);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        tvDateOfBirth = findViewById(R.id.dateOfBirth);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        userLocalStore = new UserLocalStore(this);

        spinner = findViewById(R.id.sexSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexes, android.R.layout.simple_spinner_item);
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
    }

    public void updateDate(View v) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Bundle bundle = new Bundle();

        DatePickerDialog dialog = new DatePickerDialog(
                RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "." + month + "." + year;
                tvDateOfBirth.setText(date);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createAccount(View v) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate now = LocalDate.now();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String username = etUsername.getText().toString();
        String firstName = etUsername.getText().toString();
        String lastName = etLastName.getText().toString();
        LocalDate dateOfBirth = LocalDate.parse(tvDateOfBirth.getText(), formatter);
        int age = (int) java.time.temporal.ChronoUnit.YEARS.between(dateOfBirth, now);
        String height = etHeight.getText().toString();
        String weight = etWeight.getText().toString();
        String sex = spinner.getItemAtPosition(choice).toString();

        if (username.isEmpty()) {
            etUsername.setError("Field can't be empty!");
            etUsername.requestFocus();
            return;
        } if (!userLocalStore.getUserInfo(username).username.isEmpty()) {
            etUsername.setError("Username is already taken.");
            etUsername.requestFocus();
            return;
        } if (firstName.isEmpty()) {
            etFirstName.setError("Field can't be empty!");
            etFirstName.requestFocus();
            return;
        } if (lastName.isEmpty()) {
            etLastName.setError("Field can't be empty!");
            etLastName.requestFocus();
            return;
        } if (dateOfBirth.toString().isEmpty()) {
            tvDateOfBirth.setError("Field can't be empty!");
            tvDateOfBirth.requestFocus();
            return;
        } if (height.isEmpty()) {
            etHeight.setError("Field can't be empty!");
            etHeight.requestFocus();
            return;
        } if (weight.isEmpty()) {
            etWeight.setError("Field can't be empty!");
            etWeight.requestFocus();
            return;
        } if (password.isEmpty()) {
            etPassword.setError("Field can't be empty!");
            etPassword.requestFocus();
            return;
        } if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Field can't be empty!");
            etConfirmPassword.requestFocus();
            return;
        } if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords doesn't match!");
            etConfirmPassword.requestFocus();
            return;
        } if (!checkPassword(password)) {
            etPassword.setError("Password needs to have at least 12 characters, one digit, one " +
                    "lowercase letter, one uppercase letter and one special character!");
            etPassword.requestFocus();
            return;
        }

        String salt = generateSalt();
        String securePassword = getSHA512(password, salt.getBytes());

        User registeredUser = new User(firstName, lastName, username, securePassword, salt, sex,
                dateOfBirth, age, parseFloat(height), parseFloat(weight)/*, bmi*/);
        userLocalStore.storeUserData(registeredUser);
        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
        startActivityForResult(intent, 1);
    }

    // Checks whether the password fills the requirements of a secure password or not.
    public boolean checkPassword(String password) {
        char ch;
        boolean isValid = true;
        boolean hasDigit = false;
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i<password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            }
            if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                hasSpecialChar = true;
            }
        }

        if (password.length() < 12) {
            isValid = false;
        } else if (!hasDigit) {
            isValid = false;
        } else if (!hasUpperCase) {
            isValid = false;
        } else if (!hasLowerCase) {
            isValid = false;
        } else if (!hasSpecialChar) {
            isValid = false;
        }

        return isValid;
    }

    // Generates salt for password.
    public String generateSalt() {
        byte[] salt = new byte[16];
        String stringSalt = "";
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA512PRNG");
            sr.nextBytes(salt);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< salt.length ;i++) {
                sb.append(Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1));
            }
            stringSalt = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringSalt;
    }

    public String getSHA512(String password, byte[] salt) {
        String securePassword = password;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            securePassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return securePassword;
    }

    public void backToLogIn(View v) {
        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
        startActivityForResult(intent, 1);
    }
}