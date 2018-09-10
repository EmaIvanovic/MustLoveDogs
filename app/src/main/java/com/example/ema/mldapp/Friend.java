package com.example.ema.mldapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Friend {

    public String username;

    public Friend() {
        // Default constructor required for calls to DataSnapshot.getValue(Friend.class)
    }

    public Friend(String username){
        this.username = username;
    }
}
