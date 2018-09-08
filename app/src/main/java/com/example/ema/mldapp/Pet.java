package com.example.ema.mldapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Pet{
    public String petName;
    public String breed;
    public String gender;
    public String[] descriptionTags;

    public Pet(){
        //for database
    }

    public Pet(String petName,String breed,String gender,String[] descriptionTags){
        this.petName = petName;
        this.gender = gender;
        this.breed = breed;
        this.descriptionTags = descriptionTags;
    }
}
