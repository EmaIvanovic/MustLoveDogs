package com.example.ema.mldapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final int FRIENDS_ACTIVITY = 1;
    public static final String TAG = "HomeActivity.class";
    private Toolbar toolbar;
      private FirebaseAuth mAuth;
//    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        int previous = 0;

        toolbar = findViewById(R.id.home_activity_toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationView sideNavigation = (NavigationView)findViewById(R.id.sideNavigation);
        sideNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.side_logout:
                        mAuth.signOut();
                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        return true;
                    case R.id.side_profile:
                        startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                }
                return false;
            }

        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        try{
            previous = getIntent().getExtras().getInt("previous");
        }
        catch (Exception e){
            Log.d(TAG,"Error retrieving Intent's extras");
        }

        if(previous == FRIENDS_ACTIVITY){
            loadFragment(new PeopleFragment());
            navigation.setSelectedItemId(R.id.navigation_people);

        }
        else{
            loadFragment(new FeedFragment());
            navigation.setSelectedItemId(R.id.navigation_home);
        }
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
//
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.navigation_map:
                fragment = new MapFragment();
                break;

            case R.id.navigation_home:
                fragment = new FeedFragment();
                break;

            case R.id.navigation_people:
                fragment = new PeopleFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sidebar:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
