package com.example.ema.mldapp;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateLocationTask extends AsyncTask<Context, LatLng, Long> {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;



    protected Long doInBackground(Context... context) {

        Context mctx = context[0];
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mctx);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        while(true){

            getDeviceLocation();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    protected void onProgressUpdate(LatLng... userLoc){

        double lo = userLoc[0].longitude;
        HomeActivity.currentUser.setLng(String.valueOf(lo));

        double la = userLoc[0].latitude;
        HomeActivity.currentUser.setLat(String.valueOf(la));

        DatabaseReference newPostRef = mDatabase.child("users").child(mUser.getDisplayName()).child("lat");
        newPostRef.setValue(la);

        newPostRef = mDatabase.child("users").child(mUser.getDisplayName()).child("lng");
        newPostRef.setValue(lo);

    }

    protected void onPostExecute(Long result) {

    }

    private void getDeviceLocation() {

        try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation =  (Location) task.getResult();
                            LatLng userLoc = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            onProgressUpdate(userLoc);
                        }
                    }
                });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
