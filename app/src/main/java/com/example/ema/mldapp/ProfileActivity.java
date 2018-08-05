package com.example.ema.mldapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextInputEditText username,firstname,lastname;
    private static final String TAG = "ProfileActivity.class";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = findViewById(R.id.navigation_profile);
        navigation.setOnNavigationItemSelectedListener(this);

        username = findViewById(R.id.txtUser);
        firstname = findViewById(R.id.txtName);
        lastname = findViewById(R.id.txtSurname);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        loadFragment(new HumanFragment());
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.navigation_human:
                fragment = new HumanFragment();
                break;

            case R.id.navigation_pet:
                fragment = new PetFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
