package com.example.ema.mldapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    @Override
    public void onPause() {
        super.onPause();
    }

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, null);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting adapter to recyclerview
        recyclerView.setAdapter(HomeActivity.adapter);

        EditText txtQuery = (EditText) view.findViewById(R.id.txtSearch);

        view.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtQuery = (EditText) v.getRootView().findViewById(R.id.txtSearch);
                String param = txtQuery.getText().toString();

                RadioButton rbNick = (RadioButton) v.getRootView().findViewById(R.id.rbNick);
                RadioButton rbType = (RadioButton) v.getRootView().findViewById(R.id.rbTOP);

                String attribute = "";
                if(rbNick.isChecked())
                    attribute = "nickname";
                else if(rbType.isChecked())
                    attribute = "typeOfPost";

                SearchByAtribute(attribute, param, HomeActivity.friendPosts);

            }
        });

        view.findViewById(R.id.btnCancelSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity.isSearched = false;
                HomeActivity.adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void SearchByAtribute(String attribute, String param, ArrayList<Post> posts){

        HomeActivity.searchPost.clear();

        for(int i = 0; i < posts.size(); i++){

            if (attribute.equals("nickname")) {
                if(param.equals("") || posts.get(i).getCreator().equals(param))
                    HomeActivity.searchPost.add(posts.get(i));
            }
            else if (attribute.equals("typeOfPost")){
                if(param.equals("") || posts.get(i).getTypeOfPost().equals(param))
                    HomeActivity.searchPost.add(posts.get(i));
            }
        }

        HomeActivity.isSearched = true;
        HomeActivity.adapter.notifyDataSetChanged();
    }

}
