package com.example.ema.mldapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

//    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new FeedFragment());

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
//
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

//        if(mToggle.onOptionsItemSelected(item))
//        {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
