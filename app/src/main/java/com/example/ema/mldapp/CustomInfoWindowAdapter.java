package com.example.ema.mldapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    String img;
    String title;
    private Bitmap bm;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){

        title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.txtTypeOfPost);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.txtDesc);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);

            if(marker.getTag() == null)
                return;

            Post post = (Post) marker.getTag();
            img = post.getImgPath();
            ImageView iView = (ImageView) view.findViewById(R.id.imageViewPostTile);

            if(!img.equals("")){
                try{
                    Thread t = new Thread(new Runnable() {
                    public void run() {
                            getPostImage(img);
                        }
                });
                    t.start();
                    t.join();

                    iView.setImageBitmap(bm);
                }catch(InterruptedException ie){}
            }
        }
        else{

            tvSnippet.setText("");

            ImageView iView = (ImageView) view.findViewById(R.id.imageViewPostTile);
            if(!title.equals("")){
                try{
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            getFriendImage(title);
                        }
                    });
                    t.start();
                    t.join();

                    iView.setImageBitmap(bm);

                }catch(InterruptedException ie){}
            }

        }

    }

    public void getPostImage(String imgPath){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        StorageReference ref = storage.getReference().child("images/" + mUser.getDisplayName() + "/" + imgPath + ".jpg");
        try{
            final File localFile = File.createTempFile("Images","jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getFriendImage(String nn){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        StorageReference ref = storage.getReference().child("images/" + nn + "/profileImage.jpg");
        try{
            final File localFile = File.createTempFile("Images","jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
