package com.example.ema.mldapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Pet{
    public String petName;
    public String breed;
    public String[] descriptionTags;

    public Pet(){
        //for data base
    }

    public Pet(String petName,String breed,String[] descriptionTags){
        this.petName = petName;
        this.breed = breed;
        this.descriptionTags = descriptionTags;
    }
}
