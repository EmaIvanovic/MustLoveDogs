package com.example.ema.mldapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PeopleDataAdapter extends RecyclerView.Adapter<PeopleDataAdapter.PeopleViewHolder> {
    private String[] mData;
    private ArrayList<String> mDataList;
    private Context mContext;

    public static class PeopleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public PeopleViewHolder(final View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.friendUsername_text); // Creating TextView object with the id of textView from item_row.xml
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Bundle extras = new Bundle();
                    extras.putString("username",mTextView.getText().toString());
                    Intent intent = new Intent(context, FriendProfileActivity.class);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }
    }

        public PeopleDataAdapter(ArrayList<String> myDataset,Context context) {
            mDataList = myDataset;
            mContext = context;
        }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public PeopleDataAdapter.PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext).inflate(R.layout.people_recycle_row, parent, false);
        PeopleViewHolder vh = new PeopleViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataList.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
