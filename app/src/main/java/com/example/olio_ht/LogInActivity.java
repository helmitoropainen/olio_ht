package com.example.olio_ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    TextView LogIn, SignUp;
    EditText etUsername, etPassword;
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

        LogIn.setOnClickListener(new View.OnClickListener() {
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

                // Setting username and password fields empty when user logs in.
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

    // Takes password, salt, hashed and salted password as inputs, generates the hashed password
    // and compares the two hashed and salted passwords. Returns true if the two hashed and salted
    // passwords are same and false if not.
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
