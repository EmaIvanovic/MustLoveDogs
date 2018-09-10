package com.example.ema.mldapp;

//import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//TODO compress bitmap to JPEG before uploading to storage
public class HumanFragment extends Fragment {

    private EditText firstname, lastname, aboutme;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Context context;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private Button saveHumanBtn;
    private ImageButton btnChooseFromGallery, btnOpenCamera;
    private ImageView profileImage;
    private Uri profileImagePath;
    private Uri photoURI;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int RESULT_OK = -1;
    static final int REQUEST_TAKE_PHOTO = 200;
    String mCurrentPhotoPath;
    private ProgressBar spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_human, null);
        context = inflater.getContext();
        aboutme = (EditText) view.findViewById(R.id.txtAbout);
        firstname = (EditText) view.findViewById(R.id.txtName);
        lastname = (EditText) view.findViewById(R.id.txtSurname);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        spinner = (ProgressBar)view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        saveHumanBtn = view.findViewById(R.id.buttonSaveHuman);
        btnChooseFromGallery = (ImageButton) view.findViewById(R.id.buttonAddFromGallery);
        btnOpenCamera = (ImageButton) view.findViewById(R.id.buttonOpenCamera);

        String displayName = mUser.getDisplayName();
        context = getActivity();

        DatabaseReference ref = mDatabase.child("users");
        DatabaseReference specUserRef = ref.child(displayName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usr = dataSnapshot.getValue(User.class);
                    firstname.setText(usr.firstname);
                    lastname.setText(usr.lastname);
                    aboutme.setText(usr.aboutMe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HumanFragment.class", "Database error retrieving data");
            }
        };
        specUserRef.addValueEventListener(valueEventListener);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        saveHumanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHuman();
            }
        });
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    private void uploadImage() {
        if (profileImagePath != null) {
            spinner.setVisibility(View.VISIBLE);

            StorageReference ref = storageReference.child("images/" + mUser.getDisplayName() + "/profileImage.jpg");
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
                            //Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        StorageReference ref = storageReference.child("images/" + mUser.getDisplayName() + "/profileImage.jpg");
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
        StorageReference ref = storage.getReference().child("images/" + mUser.getDisplayName() + "/profileImage.jpg");
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


    private void saveHuman(){
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String ame = aboutme.getText().toString();
        User u = new User(mUser.getEmail(),fname,lname,mUser.getDisplayName(),ame);
        mDatabase.child("users").child(mUser.getDisplayName()).setValue(u);
        Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
    }
}
