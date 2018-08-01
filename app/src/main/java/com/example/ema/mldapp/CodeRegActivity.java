package com.example.ema.mldapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CodeRegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_reg);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btnProceed)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }
}
