package com.example.ema.mldapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(PostViewHolder holder, int position) {
        //getting the product of the specified position
        Post p = HomeActivity.posts.get(position);

        //binding the data with the viewholder views
        holder.txtDesc.setText(p.getDesc());
        holder.txtTypeOfPost.setText(p.getTypeOfPost());
        //holder.imgView.setImageURI(p.getPictureURI());

    }


    @Override
    public int getItemCount() {
        return HomeActivity.posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView txtDesc, txtTypeOfPost;
        //ImageView imgView;

        public PostViewHolder(View itemView) {
            super(itemView);

            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtTypeOfPost = itemView.findViewById(R.id.txtTypeOfPost);
            //imgView = itemView.findViewById(R.id.imageView);
        }
    }

}
