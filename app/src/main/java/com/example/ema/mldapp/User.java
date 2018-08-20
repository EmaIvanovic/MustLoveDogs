package com.example.ema.mldapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public String firstname;
    public String lastname;
    public String username;
    public String aboutMe;
    public String profileImageUrl;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstName, String lastName ,String username,String aboutMe,String profileImageUrl) {
        this.email = email;
        this.firstname = firstName;
        this.lastname = lastName;
        this.username = username;
        this.aboutMe = aboutMe;
        this.profileImageUrl = profileImageUrl;
    }
}
