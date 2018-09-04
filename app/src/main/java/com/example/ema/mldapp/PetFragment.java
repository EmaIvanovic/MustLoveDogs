package com.example.ema.mldapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PetFragment extends Fragment {
    private EditText petName, breed, tags;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Context context;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private Button savePetBtn;
    private ImageButton btnChooseFromGallery, btnOpenCamera;
    private ImageView profileImage;
    private Uri profileImagePath;
    private String displayName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, null);

        context = inflater.getContext();
        petName = (EditText) view.findViewById(R.id.txtPetName);
        breed = (EditText) view.findViewById(R.id.txtBreed);
        tags = (EditText) view.findViewById(R.id.txtTags);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        savePetBtn = view.findViewById(R.id.buttonSavePet);
        displayName = mUser.getDisplayName();
        context = getActivity();

        DatabaseReference ref = mDatabase.child("users");
        DatabaseReference specUserRef = ref.child(displayName);

//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    User usr = dataSnapshot.getValue(User.class);
//                    if(usr != null){
//                        if(usr.pet.petName != null)
//                            petName.setText(usr.pet.petName);
//                        if(usr.pet.breed != null)
//                            breed.setText(usr.pet.breed);
//                        if(usr.pet.descriptionTags != null){
//                            String descTags = "";
//                            for (String s:usr.pet.descriptionTags
//                            ) {
//                                descTags += s + ", ";
//                            }
//                            descTags = descTags.substring(0,descTags.length() - 2);
//                            tags.setText(descTags);}
//                }
//
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("PetFragment.class", "Database error retrieving data");
//            }
//        };
//        specUserRef.addValueEventListener(valueEventListener);
        savePetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePet();
            }
        });
        return view;
    }
    private void savePet(){
        String pName = petName.getText().toString();
        String breedd = breed.getText().toString();
        String desctagsTogether = tags.getText().toString();
        String tagsSplit[] = desctagsTogether.split(",");
        for (String s:tagsSplit
             ) {
            s = s.trim();
        }
            Pet p = new Pet(pName,breedd,tagsSplit);
            mDatabase.child("users").child(displayName).child("pet").setValue(p);
            Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();

    }
}

