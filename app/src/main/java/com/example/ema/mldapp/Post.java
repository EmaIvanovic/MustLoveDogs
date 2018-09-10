package com.example.ema.mldapp;

import java.io.Serializable;

//Klasa namenjena za post
public class Post implements Serializable {

    private String desc;
    private String typeOfPost;
    //private String pictureURI;

    /*public String getPictureURI() {
        return pictureURI;
    }*/
    public String getDesc() {
        return desc;
    }
    public String getTypeOfPost() {
        return typeOfPost;
    }

    public Post(String d, String t/*, String p*/) {
        desc = d;
        typeOfPost = t;
        //pictureURI = p;
    }
}
