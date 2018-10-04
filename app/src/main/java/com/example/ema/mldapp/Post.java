package com.example.ema.mldapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

//Klasa namenjena za post
public class Post implements Serializable {

    private String desc;
    private String typeOfPost;
    private String lat;
    private String lng;
    private String imgPath;
    private String creator;

    public String getLat() { return lat; }
    public String getLng() { return lng; }
    public String getDesc() { return desc; }
    public String getTypeOfPost() { return typeOfPost; }
    public String getImgPath() { return imgPath; }
    public String getCreator() { return creator; }

    public Post(String d, String t, String la, String lg, String i, String c) {
        desc = d;
        typeOfPost = t;
        lat = la;
        lng = lg;
        imgPath = i;
        creator = c;
    }
}