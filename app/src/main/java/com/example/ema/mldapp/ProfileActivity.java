package com.example.ema.mldapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextInputEditText username,lastname;
    private EditText firstname;
    private static final String TAG = "ProfileActivity.class";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageButton btnChooseFromGallery, btnOpenCamera;
    private ImageView profileImage;
    private final String FRAGMENT_HUMAN="human";
    private final String FRAGMENT_PET="pet";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = findViewById(R.id.navigation_profile);
        navigation.setOnNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();
        loadFragment(new HumanFragment(),FRAGMENT_HUMAN);
    }

    private boolean loadFragment(Fragment fragment,String TAG){
        if(fragment != null){

            fragmentManager
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
        String tag = null;

        switch (item.getItemId()){
            case R.id.navigation_human:
                fragment = fragmentManager.findFragmentByTag(FRAGMENT_HUMAN);
                tag = FRAGMENT_HUMAN;
                if (fragment == null) {
                    fragment = new HumanFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, FRAGMENT_HUMAN)
                            .commit();
                }
                break;

            case R.id.navigation_pet:
                fragment = fragmentManager.findFragmentByTag(FRAGMENT_PET);
                tag=FRAGMENT_PET;
                if (fragment == null) {
                    fragment = new PetFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, FRAGMENT_PET)
                            .commit();
                }
                break;
        }
        return loadFragment(fragment,tag);
    }


}
