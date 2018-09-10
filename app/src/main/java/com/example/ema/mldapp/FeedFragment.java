package com.example.ema.mldapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    //private FirebaseAuth mAuth;
    //private FirebaseUser user;

    //private TextView feedText;

    @Override
    public void onPause() {
        super.onPause();
    }

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, null);

        /*mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        feedText = view.findViewById(R.id.feedTextView);
        String message = "Hello " + user.getDisplayName() + "!";
        feedText.setText(message);*/

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting adapter to recyclerview
        recyclerView.setAdapter(HomeActivity.adapter);

        return view;
    }
}
