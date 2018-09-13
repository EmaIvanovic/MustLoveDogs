package com.example.ema.mldapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {
    public String email;
    public String firstname;
    public String lastname;
    public String username;
    public String joined;
    public String activityLevel;
    public String aboutMe;
    public String profileImageUrl;
    public Pet pet;
    public ArrayList<Friend> friends;
    public int activityPoints;




    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstName, String lastName ,String username,String aboutMe, Pet pet, ArrayList<Friend> friends) {
        this.email = email;
        this.firstname = firstName;
        this.lastname = lastName;
        this.username = username;
        this.aboutMe = aboutMe;
        this.profileImageUrl = profileImageUrl;
        this.pet = pet;
        this.friends = friends;
    }

    public User(String email, String firstName, String lastName ,String username,String aboutMe){
        this.email = email;
        this.firstname = firstName;
        this.lastname = lastName;
        this.username = username;
        this.aboutMe = aboutMe;
    }
    public User(String email, String firstName, String lastName ,String username,String aboutMe,String joined, String activityLevel,int activityPoints){
        this.email = email;
        this.firstname = firstName;
        this.lastname = lastName;
        this.username = username;
        this.aboutMe = aboutMe;
        this.activityLevel = activityLevel;
        this.joined = joined;
        this.activityPoints = activityPoints;
    }
}
