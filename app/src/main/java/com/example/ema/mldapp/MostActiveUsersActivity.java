package com.example.ema.mldapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class MostActiveUsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String[] data;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ArrayList<String> dataArray;
    private ArrayList<User> dataArrayUsers;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_active_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_level_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MostActiveUsersActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String displayName = mUser.getDisplayName();

        mDatabase =  FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("users");
        DatabaseReference specUserRef = ref.child(displayName);
        DatabaseReference friendsRef = specUserRef.child("friends");


        dataArray = new ArrayList<>();
        dataArrayUsers = new ArrayList<User>();
       // mAdapter = new PeopleDataAdapter(dataArray,MostActiveUsersActivity.this);
        //mRecyclerView.setAdapter(mAdapter);


        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                        Friend friend = friendSnapshot.getValue(Friend.class);
                        if(friend != null)
                            dataArray.add(friend.username);

                    }

                    //mAdapter = new PeopleDataAdapter(dataArray,context);
                    //mRecyclerView.setAdapter(mAdapter);
                    //Toast.makeText(context, "Success reading friends", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MostActiveUsersActivity.this, "Failed to read data for RecycleView", Toast.LENGTH_LONG).show();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //User user = userSnapshot.getValue(User.class);

                        if(userSnapshot == null)
                            return;

                        String uname = (String) dataSnapshot.child("username").getValue();
                        int ap = (int) dataSnapshot.child("activityPoints").getValue();

                        User user = new User();
                        user.username = uname;

                        user.activityPoints = ap;


                        if (dataArray.contains(user.username))
                            dataArrayUsers.add(user);


                    }
                    ArrayList<String> pom = new ArrayList<String>();
                    sortList(dataArrayUsers);
                    for (User u : dataArrayUsers
                         ) {
                        String pomStr = u.username + "\t(" + u.activityPoints + ")";
                        pom.add(pomStr);
                    }

                    mAdapter = new PeopleDataAdapter(pom,MostActiveUsersActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    //Toast.makeText(context, "Success reading friends", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MostActiveUsersActivity.this, "Failed to read data for RecycleView", Toast.LENGTH_LONG).show();
            }
        });


    }

    void sortList(ArrayList<User> users){

            int n = users.size();

            // One by one move boundary of unsorted subarray
            for (int i = 0; i < n-1; i++)
            {
                // Find the minimum element in unsorted array
                int max_idx = i;
                for (int j = i+1; j < n; j++)
                    if (users.get(j).activityPoints > users.get(max_idx).activityPoints)
                        max_idx = j;

                // Swap the found minimum element with the first
                // element
                User temp = users.get(max_idx);
                users.set(max_idx, users.get(i));
                users.set(i, temp);
            }
    }

}