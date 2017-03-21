package com.example.claudiochicodev.easyrestaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
    }

    public void waitingStaffClicked(View v){
        Intent i = new Intent(getBaseContext(), SelectTableActivity.class);
        startActivity(i);
    }
}
