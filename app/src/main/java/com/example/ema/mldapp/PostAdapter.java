package com.example.ema.mldapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

//Adapter za RecyclerView iz FeedFragment
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //getting the context and product list with constructor
    public PostAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_post, null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        //getting the product of the specified position
        Post p = null;

        if(HomeActivity.isSearched)
            p = HomeActivity.searchPost.get(position);
        else
            p = HomeActivity.friendPosts.get(position);

        //binding the data with the viewholder views
        holder.txtDesc.setText(p.getDesc());
        holder.txtTypeOfPost.setText(p.getTypeOfPost());
        holder.txtCreator.setText("by " + p.getCreator());

        String img = p.getImgPath();

        if(!img.equals("")){

            StorageReference ref = storage.getReference().child("images/" + p.getCreator() + "/" + img + ".jpg");

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(mCtx)
                            .load(uri)
                            .into(holder.imageView)
                            .waitForLayout();
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        int cnt = 0;

        if(HomeActivity.isSearched)
            cnt = HomeActivity.searchPost.size();
        else
            cnt = HomeActivity.friendPosts.size();

        return cnt;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView txtDesc, txtTypeOfPost, txtCreator;
        ImageView imageView;

        public PostViewHolder(View itemView) {
            super(itemView);

            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtTypeOfPost = itemView.findViewById(R.id.txtTypeOfPost);
            imageView = itemView.findViewById(R.id.imageViewPost);
            txtCreator = itemView.findViewById(R.id.txtCreator);
        }
    }

}
