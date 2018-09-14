package com.example.ema.mldapp;

import java.io.Serializable;

//Klasa namenjena za post
public class Post implements Serializable {

    private String desc;
    private String typeOfPost;
    private String lat;
    private String lng;
    private String imgPath;

    public String getLat() { return lat; }
    public String getLng() { return lng; }
    public String getDesc() { return desc; }
    public String getTypeOfPost() { return typeOfPost; }
    public String getImgPath() { return imgPath; }

    public Post(String d, String t, String la, String lg, String i) {
        desc = d;
        typeOfPost = t;
        lat = la;
        lng = lg;
        imgPath = i;
    }
}