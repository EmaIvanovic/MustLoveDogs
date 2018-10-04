package com.example.ema.mldapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    public CustomInfoWindowAdapter(Context context) {

        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        ImageView imView = (ImageView) view.findViewById(R.id.imageViewPostTile);
        imView.setVisibility(View.INVISIBLE);

        String title = marker.getTitle();
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
            String img = post.getImgPath();
            final ImageView iView = (ImageView) view.findViewById(R.id.imageViewPostTile);
            iView.setVisibility(view.VISIBLE);
            if(!img.equals("")){
                StorageReference ref = storage.getReference().child("images/" + post.getCreator() + "/" + img + ".jpg");
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(mContext)
                                .load(uri)
                                .into(iView);
                    }});
            } }
        else{

            tvSnippet.setText("");

            final ImageView iView = (ImageView) view.findViewById(R.id.imageViewPostTile);
            iView.setVisibility(view.VISIBLE);
            if(!title.equals("")){
                StorageReference ref = storage.getReference().child("images/" + title + "/profileImage.jpg");
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(mContext)
                                .load(uri)
                                .into(iView);
                    }});
            } }
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