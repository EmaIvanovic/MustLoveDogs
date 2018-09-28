package com.example.ema.mldapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static User currentUser;
    public static double m_radius;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    //    private DrawerLayout mDrawerLayout;
    //    private ActionBarDrawerToggle mToggle;

    public static ArrayList<Post> posts;
    public static PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser = new User();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        posts = new ArrayList<Post>();

        loadPosts();

        adapter = new PostAdapter(this);

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

        loadFragment(new FeedFragment());
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
//
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        savePosts();
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

    //
//     case R.id.side_logout:
//            mAuth.signOut();
//    startActivity(new Intent(HomeActivity.this,MainActivity.class));
//                break;
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

    public void onClick1(View v) {
        if(v.getId() == R.id.btnAddPost)
        {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivityForResult(intent,7);

        }
        else if(v.getId() == R.id.btnRadius)
        {
            EditText t = findViewById(R.id.txtRadius);
            m_radius = Double.parseDouble(t.getText().toString());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 7) {
            if(resultCode == RESULT_OK) {
                Post newPost = (Post) data.getExtras().getSerializable("New_post");
                posts.add(newPost);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void loadPosts() {
        mDatabase.child("users").child(mUser.getDisplayName()).child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> p = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                int i = 0;
                if(p == null){
                    return;
                }
                while(i < p.size()){
                    String d = p.get(i).get("desc");
                    String t = p.get(i).get("typeOfPost");
                    String la =  p.get(i).get("lat");
                    String lng = p.get(i).get("lng");
                    String img = p.get(i).get("imgPath");

                    posts.add(new Post(d,t, la, lng, img));
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void savePosts(){
        DatabaseReference newPostRef = mDatabase.child("users").child(mUser.getDisplayName()).child("posts");
        newPostRef.setValue(posts);
    }
}