package com.example.ema.mldapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//TODO sredi ovu klasu
public class PetFragment extends Fragment {
    private EditText petName, breed, tags;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Context context;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private Button savePetBtn;
    private RadioButton femaleRadioBtn, maleRadioBtn;
    private ImageButton btnChooseFromGallery, btnOpenCamera;
    private ImageView profileImage;
    private Uri profileImagePath;
    private String displayName;
    private Uri photoURI;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int RESULT_OK = -1;
    static final int REQUEST_TAKE_PHOTO = 200;
    String mCurrentPhotoPath;
    private ProgressBar spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, null);
        context = inflater.getContext();
        profileImage = (ImageView) view.findViewById(R.id.profileImagePet);
        petName = (EditText) view.findViewById(R.id.txtPetName);
        breed = (EditText) view.findViewById(R.id.txtBreed);
        tags = (EditText) view.findViewById(R.id.txtTags);
        femaleRadioBtn = (RadioButton) view.findViewById(R.id.btnFemale);
        maleRadioBtn = (RadioButton) view.findViewById(R.id.btnMale);
        spinner = (ProgressBar)view.findViewById(R.id.progressBarPet);
        spinner.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        savePetBtn = view.findViewById(R.id.buttonSavePet);
        btnChooseFromGallery = (ImageButton) view.findViewById(R.id.buttonAddPetFromGallery3);
        btnOpenCamera = (ImageButton) view.findViewById(R.id.buttonOpenCameraForPet);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        displayName = mUser.getDisplayName();
        context = getActivity();

        DatabaseReference ref = mDatabase.child("users");
        DatabaseReference userRef = ref.child(displayName);
        DatabaseReference usersPetRef = userRef.child("pet");
        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Pet pet = dataSnapshot.getValue(Pet.class);
                    if(pet != null){
                        if(pet.petName != null)
                            petName.setText(pet.petName);
                        if(pet.breed != null)
                            breed.setText(pet.breed);
                        if(pet.gender != null)
                        {
                            if(pet.gender.equals("f"))
                                femaleRadioBtn.toggle();
                            else if(pet.gender.equals("m"))
                                maleRadioBtn.toggle();
                        }
                        if(pet.descriptionTags != null){
                            String descTags = "";
                            for (String s:pet.descriptionTags
                            ) {
                                descTags += s + ", ";
                            }
                            descTags = descTags.substring(0,descTags.length() - 2);
                            tags.setText(descTags);}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("PetFragment.class", "Database error retrieving data");
            }
        };
        usersPetRef.addValueEventListener(valueEventListener);
        savePetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePet();
            }
        });
        setProfileImageView();
        return view;
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), PICK_IMAGE_REQUEST);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(context,
                        "com.example.ema.mldapp.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, HumanFragment.REQUEST_TAKE_PHOTO);
            }
        }

    }

    private void uploadImage() {
        if (profileImagePath != null) {
            spinner.setVisibility(View.VISIBLE);

            StorageReference ref = storageReference.child("images/" + mUser.getDisplayName() + "/pet.jpg");
            ref.putFile(profileImagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                            setProfileImageView();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            spinner.setVisibility(View.GONE);
                        }
                    });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            profileImagePath = data.getData();
            uploadImage();
        }
        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            uploadCameraPhoto(photoURI);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadCameraPhoto(Uri photoUri){
        spinner.setVisibility(View.VISIBLE);
        StorageReference ref = storage.getReference().child("images/" + mUser.getDisplayName() + "/pet.jpg");
        ref.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        setProfileImageView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        spinner.setVisibility(View.GONE);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });

    }

    private void setProfileImageView() {
        StorageReference ref = storage.getReference().child("images/" + mUser.getDisplayName() + "/pet.jpg");
        try {
            final File localFile = File.createTempFile("Images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profileImage.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void savePet(){
        String pName = petName.getText().toString();
        if(pName.equals(""))
        {
            Toast.makeText(context,"Fill in your pet's name",Toast.LENGTH_LONG).show();
            return;
        }
        String pBreed = breed.getText().toString();
        if(pBreed.equals(""))
        {
            Toast.makeText(context,"Fill in your pet's breed",Toast.LENGTH_LONG).show();
            return;
        }
        String pGender;
        if(femaleRadioBtn.isChecked())
            pGender = "f";
        else if(maleRadioBtn.isChecked())
            pGender = "m";
        else{
            Toast.makeText(context,"Choose your pet's gender",Toast.LENGTH_LONG).show();
            return;
        }
        String desctags = tags.getText().toString();
        String pTags[] = null;
        if (!desctags.equals("")) {
            pTags = desctags.split(",");
            for (String s : pTags) {
                s = s.trim();
            }
        }
            Pet p = new Pet(pName,pBreed,pGender,pTags);
            mDatabase.child("users").child(displayName).child("pet").setValue(p);
            Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();

    }
}

