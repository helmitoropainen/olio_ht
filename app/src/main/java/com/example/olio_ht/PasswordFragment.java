package com.example.olio_ht;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;

public class PasswordFragment extends Fragment {

    View view;
    Button btChangePassword;
    EditText etPassword;
    EditText etConfirmPassword;
    RegisterActivity ra = new RegisterActivity();
    UserLocalStore uls;


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        uls = new UserLocalStore(getActivity().getBaseContext());
        btChangePassword = view.findViewById(R.id.button3);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void changePassword() {
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
    if (password.isEmpty()) {
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
    } if (!ra.checkPassword(password)) {
        etPassword.setError("Password needs to have at least 12 characters, one digit, one " +
                "lowercase letter, one uppercase letter and one special character!");
        etPassword.requestFocus();
        return;
    }
        String salt = ra.generateSalt();
        String securePassword = ra.getSHA512(password, salt.getBytes());

        String username =uls.getUserLoggedIn();
        String firstName = uls.getUserInfo(username).firstName;
        String lastName = uls.getUserInfo(username).lastName;
        LocalDate dateOfBirth = uls.getUserInfo(username).dateOfBirth;
        int age = uls.getUserInfo(username).age;
        float height = uls.getUserInfo(username).height;
        float weight = uls.getUserInfo(username).weight;
        String sex = uls.getUserInfo(username).sex;
        long sleepGoal = uls.getUserInfo(username).sleepGoal;
        long caloriesGoal = uls.getUserInfo(username).caloriesGoal;

        User changedUser = new User(firstName, lastName, username, securePassword, salt, sex,
                dateOfBirth, age, height, weight, caloriesGoal, sleepGoal);
        uls.storeUserData(changedUser);

        Context context = getContext();
        CharSequence text = "Password changed successfully.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
