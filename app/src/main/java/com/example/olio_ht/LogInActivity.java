package com.example.olio_ht;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    TextView LogIn;
    TextView SignUp;
    EditText etUsername;
    EditText etPassword;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.app_name);

        LogIn = (TextView) findViewById(R.id.logInView);
        SignUp = (TextView) findViewById(R.id.signUpView);
        etUsername = findViewById(R.id.usernameInput);
        etPassword = findViewById(R.id.passwordInput);

        userLocalStore = new UserLocalStore(this);
        System.out.println("TIEDOSTOSIJAINTI: " + LogInActivity.this.getFilesDir());
        // Tarkistamista varten.

        LogIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                User user = userLocalStore.getUserInfo(username);
                String securePassword = userLocalStore.getUserInfo(username).password;
                byte[] salt = userLocalStore.getUserInfo(username).salt.getBytes();
                if (username.isEmpty()) {
                    etUsername.setError("Field can't be empty!");
                    etUsername.requestFocus();
                    return;
                } if (!user.username.equals(username)) {
                    etUsername.setError("Username not found. Please sign up.");
                    etUsername.requestFocus();
                    return;
                } if (password.isEmpty()) {
                    etPassword.setError("Field can't be empty!");
                    etPassword.requestFocus();
                    return;
                } if (!passwordCheck(password, securePassword, salt)) {
                    etPassword.setError("Wrong password.");
                    etPassword.requestFocus();
                    return;
                }
                etUsername.setText("");
                etPassword.setText("");
                userLocalStore.setUserLoggedIO(user.username);
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    public boolean passwordCheck(String password, String securePassword, byte[] salt) {
        Boolean match = false;
        RegisterActivity ra = new RegisterActivity();
        String hashedPassword = ra.getSHA512(password, salt);
        if (securePassword.equals(hashedPassword)) {
            match = true;
        }
        return match;
    }

}