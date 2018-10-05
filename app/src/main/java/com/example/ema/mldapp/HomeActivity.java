package com.example.ema.mldapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static User currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    public static ArrayList<Post> userPosts;
    public static ArrayList<Post> friendPosts;
    public static ArrayList<Post> searchPost;
    public static Boolean isSearched = false;
    public static ArrayList<String> friendNicks;
    public static PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser = new User();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userPosts = new ArrayList<Post>();
        friendPosts = new ArrayList<Post>();
        searchPost = new ArrayList<>();
        friendNicks = new ArrayList<>();

        loadPosts();
        getFriends();

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
                        return true;
                    case R.id.side_leaderboard:
                        startActivity(new Intent(HomeActivity.this,MostActiveUsersActivity.class));
                        return  true;
                }
                return false;
            }

        });

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new FeedFragment());

        new UpdateLocationTask().execute(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        savePosts();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 7) {
            if(resultCode == RESULT_OK) {
                Post newPost = (Post) data.getExtras().getSerializable("New_post");
                userPosts.add(newPost);
                adapter.notifyDataSetChanged();
            }
        }
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

    public void onClick1(View v) {
        if(v.getId() == R.id.btnAddPost)
        {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivityForResult(intent,7);

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
                    String c = p.get(i).get("creator");

                    userPosts.add(new Post(d,t, la, lng, img, c));
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
        newPostRef.setValue(userPosts);
        DatabaseReference newPostRef1 = mDatabase.child("users").child(mUser.getDisplayName());//.child("activityPoints");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long points = 0;
                if(dataSnapshot.child("activityPoints").getValue() != null)
                {
                    points = (long)dataSnapshot.child("activityPoints").getValue();
                    points += 1;
                }
                dataSnapshot.child("activityPoints").getRef().setValue(points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HomeActivity.class", "Database error retrieving data for totalPoints");
            }
        };
        newPostRef1.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getFriendsPosts() {

        friendPosts.clear();

        for(int i = 0; i < friendNicks.size(); i++)
        {
            mDatabase.child("users").child(friendNicks.get(i)).child("posts").addValueEventListener(new ValueEventListener() {
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
                        String c = p.get(i).get("creator");

                        friendPosts.add(new Post(d,t, la, lng, img, c));
                        i++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void getFriends() {
        DatabaseReference friendsRef = mDatabase.child("users").child(mUser.getDisplayName()).child("friends");

        friendsRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectFriendNicks((Map<String,Object>) dataSnapshot.getValue());
                        getFriendsPosts();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void collectFriendNicks(Map<String,Object> users) {

        if(users != null)
        {
            for (Map.Entry<String, Object> entry : users.entrySet()){
                Map singleUser = (Map) entry.getValue();
                friendNicks.add((String) singleUser.get("username"));
                currentUser.friends.add(new Friend((String) singleUser.get("username")));
            }

            for(int i = 0; i < friendNicks.size(); i++)
            {
                mDatabase.child("users").child(friendNicks.get(i)).child("lat").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mDatabase.child("users").child(friendNicks.get(i)).child("lng").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

}