package com.example.ema.mldapp;

import android.annotation.TargetApi;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Comparator;

public class MostActiveUsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CheckBox sortCheckBox;
    private Context context;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String[] data;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ArrayList<String> dataArray;
    private ArrayList<User> dataArrayUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_active_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_level_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
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
        mAdapter = new PeopleDataAdapter(dataArray,context);
        mRecyclerView.setAdapter(mAdapter);


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
                Toast.makeText(context, "Failed to read data for RecycleView", Toast.LENGTH_LONG).show();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if(user != null) {
                            if (dataArray.contains(user.username))
                                dataArrayUsers.add(user);
                        }

                    }

                    @TargetApi(14)
                    dataArrayUsers.sort(new Comparator<User>() {
                        @Override
                        public int compare(User user, User t1) {
                            return (user.activityPoints - t1.activityPoints);
                        }
                    });
                    mAdapter = new PeopleDataAdapter(dataArray,context);
                    mRecyclerView.setAdapter(mAdapter);
                    //Toast.makeText(context, "Success reading friends", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to read data for RecycleView", Toast.LENGTH_LONG).show();
            }
        });


    }

}
