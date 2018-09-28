package com.example.ema.mldapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

//Adapter za RecyclerView iz FeedFragment
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    String img;
    private Bitmap bm;

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
    public void onBindViewHolder(PostViewHolder holder, int position) {
        //getting the product of the specified position
        Post p = HomeActivity.posts.get(position);

        //binding the data with the viewholder views
        holder.txtDesc.setText(p.getDesc());
        holder.txtTypeOfPost.setText(p.getTypeOfPost());

        img = p.getImgPath();
        if(!img.equals("")){
            try{
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        getPostImage(img);
                    }
                });
                t.start();
                t.join();

                holder.imageView.setImageBitmap(bm);
            }catch(InterruptedException ie){}
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

    @Override
    public int getItemCount() {
        return HomeActivity.posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView txtDesc, txtTypeOfPost;
        ImageView imageView;

        public PostViewHolder(View itemView) {
            super(itemView);

            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtTypeOfPost = itemView.findViewById(R.id.txtTypeOfPost);
            imageView = itemView.findViewById(R.id.imageViewPost);
        }
    }

}
