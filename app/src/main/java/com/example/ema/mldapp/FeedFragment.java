package com.example.ema.mldapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FeedFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView feedText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, null);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        feedText = view.findViewById(R.id.feedTextView);
        String message = "Hello " + user.getDisplayName() + "!";
        feedText.setText(message);

        return view;
    }
}
