package com.example.ema.mldapp;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class Friend implements Serializable {

    public String username;
    public double lat;
    public double lng;

    public double getLng() { return lng; }
    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }

    public Friend() {
        // Default constructor required for calls to DataSnapshot.getValue(Friend.class)
    }

    public Friend(String username){
        this.username = username;
    }
}
