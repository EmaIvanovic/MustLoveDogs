package com.example.ema.mldapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleFragment extends Fragment {
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people, null);

        context = this.getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.people_recycler_view);

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

        DatabaseReference specFriendRef = friendsRef.child("EmaIv");
        specFriendRef.setValue(new Friend("EmaIv"));
        DatabaseReference specFriendRef1 = friendsRef.child("OpetNovi");
        specFriendRef1.setValue(new Friend("OpetNovi"));
        DatabaseReference specFriendRef2 = friendsRef.child("Mlaki");
        specFriendRef2.setValue(new Friend("Mlaki"));



        dataArray = new ArrayList<>();
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
                if(!sortCheckBox.isChecked())

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
        return rootView;
    }
}
