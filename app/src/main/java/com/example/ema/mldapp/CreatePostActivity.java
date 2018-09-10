package com.example.ema.mldapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;

//Ekran za dodavanje postova, prosledjuje objekat klase Post HomeActivity-ju
public class CreatePostActivity extends AppCompatActivity {

    //private ImageButton btnChooseFromGallery;
    //private final int GALLERY_REQUEST = 71;
    //private Uri imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        /*btnChooseFromGallery = (ImageButton) findViewById(R.id.btnAddFromGallery);
        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });*/
    }

    /*void chooseImage(){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select picture"), GALLERY_REQUEST);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgPath = data.getData();
        }*/
    }

    public void onClick2(View v) {
        if(v.getId() == R.id.btnPost)
        {
            TextInputEditText txtInput = (TextInputEditText) findViewById(R.id.txtDescriptionText);
            String desc = txtInput.getText().toString();

            String typeOfPost = "";
            RadioButton r1 = (RadioButton) findViewById(R.id.radioPost);
            if(r1.isChecked()) {
                typeOfPost = "Post";
            }
            RadioButton r2 = (RadioButton) findViewById(R.id.radioLostDog);
            if(r2.isChecked()) {
                typeOfPost = "Lost dog!";
            }
            RadioButton r3 = (RadioButton) findViewById(R.id.radioFoundDog);
            if(r3.isChecked()) {
                typeOfPost = "Found dog!";
            }
            /*String picUri = "";
            if(imgPath != null){
                picUri = imgPath.toString();
            }*/
            Post newPost = new Post(desc, typeOfPost/*, picUri*/);

            Intent intent = new Intent();
            intent.putExtra("New_post", newPost);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
