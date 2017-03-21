package com.example.claudiochicodev.easyrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loging;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.LoginEmail_editText);
        password = (EditText) findViewById(R.id.LoginPassword_editText);
        loging = (Button) findViewById(R.id.Login_button);
        register = (TextView) findViewById(R.id.LoginRegister_textView);

        SharedResources sharedResources = new SharedResources(this);    //Initialize all shared Resources.
    }

    public void loginClicked(View v){

        boolean cancel = false;
        View focusView = null;
        String emailstr = email.getText().toString();
        String passwordstr = password.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(passwordstr)) {
            password.setError("Password must be at least 6 characters long.");
            focusView = password;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(emailstr)) {
            email.setError("Email field can't be empty.");
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(emailstr)) {
            email.setError("Enter a valid email.");
            focusView = email;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            if (SharedResources.dbHandler.isUserInDB(emailstr, passwordstr)){
                SharedResources.UserName = emailstr;
                SharedResources.HashedPassword = SharedResources.hashString(passwordstr);
                Intent i = new Intent(getBaseContext(), SelectModeActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(this, "Login failed. Check your email and password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void registerClicked(View v){
        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(i);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
