package com.example.ema.mldapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FriendProfileActivity extends AppCompatActivity {
    public static final int FRIENDS_ACTIVITY=1;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextView username_TextView,aboutMe,activityLevel,joined, fullname, petname, petBreed, petAge
            , petGender;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private String friendsUsername;
    private ImageView friend,dog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutMe = findViewById(R.id.aboutFriend_tv);
        fullname = findViewById(R.id.friendFullName);
        activityLevel = findViewById(R.id.activity_level_tv);
        joined = findViewById(R.id.joined_tv);
        friend = findViewById(R.id.fProfileImageView);
        dog =  findViewById(R.id.fProfileImageView2);
        petname =  findViewById(R.id.fPetName_tv);
        petBreed =  findViewById(R.id.fBreed_tv);
        petGender =  findViewById(R.id.fGender_tv);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String displayName = mUser.getDisplayName();


        friendsUsername = getIntent().getStringExtra("username");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("users");
        DatabaseReference specUserRef = ref.child(friendsUsername);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fn = (String) dataSnapshot.child("firstname").getValue();
                String ln = (String) dataSnapshot.child("lastname").getValue();

                String j = (String) dataSnapshot.child("joined").getValue();
                String al = (String) dataSnapshot.child("activityLevel").getValue();
                String ame = (String) dataSnapshot.child("aboutMe").getValue();
                String pName= (String) dataSnapshot.child("pet").child("petName").getValue();
                String pBreed= (String) dataSnapshot.child("pet").child("breed").getValue();
                String pGender = (String) dataSnapshot.child("pet").child("gender").getValue();

                activityLevel.setText(al);
                joined.setText(j);
                aboutMe.setText(ame);
                fullname.setText(fn + " " + ln);

                petname.setText(pName);
                petBreed.setText(pBreed);
                petGender.setText(pGender);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FriendsActivity.class", "Database error retrieving data");
            }
        };
        specUserRef.addValueEventListener(valueEventListener);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        setFriendImageView();
        sePetImageView();

        username_TextView = findViewById(R.id.friendUsername_tv);
        String value = getIntent().getStringExtra("username");
        username_TextView.setText(value);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent= new Intent(this,HomeActivity.class);
                intent.putExtra("previous",FRIENDS_ACTIVITY);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void setFriendImageView(){
        StorageReference ref = storage.getReference().child("images/" + friendsUsername + "/profileImage.jpg");

        try {
            final File localFile = File.createTempFile("Images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    friend.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sePetImageView(){
        StorageReference ref = storage.getReference().child("images/" + friendsUsername + "/pet.jpg");
        try {
            final File localFile = File.createTempFile("Images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    dog.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
