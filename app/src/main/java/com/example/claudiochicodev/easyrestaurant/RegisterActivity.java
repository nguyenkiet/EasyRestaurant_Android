package com.example.claudiochicodev.easyrestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.spec.PBEKeySpec;

public class RegisterActivity extends AppCompatActivity {

    TextView dbTesting;
    EditText email;
    EditText password;
    Button createUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.RegisterEmail_editText);
        password = (EditText) findViewById(R.id.RegisterPassword_editText);
        createUser = (Button) findViewById(R.id.Register);
        dbTesting = (TextView) findViewById(R.id.RegisterDB_textView);
        dbTesting.setText(SharedResources.dbHandler.databaseToString());
    }

    public void createUserClicked(View v){

        boolean cancel = false;
        View focusView = null;
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString(); //Add some controlled randomness to the original because... why not?

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.setError("Email field can't be empty.");
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(emailStr)) {
            email.setError("Incorrect format. Please enter a valid email address.");
            focusView = email;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (!isPasswordValid(password.getText().toString())) {
            password.setError("Password must be at least 6 characters long.");
            focusView = password;
            cancel = true;
        }
        //Check for duplicate user in DB
        else if (SharedResources.dbHandler.isUserInDB(emailStr)){
            email.setError("Email is already registered. Please use a different account.");
            focusView = email;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            String passwordHash = SharedResources.hashString(passwordStr);
            Toast.makeText(this, "New account created: " + emailStr + SharedResources.dbHandler.isUserInDB(emailStr), Toast.LENGTH_SHORT).show();
            SharedResources.dbHandler.addUser(emailStr, passwordHash);
        }

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }
}
