package com.example.ema.mldapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class PeopleFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people, null);

        context = this.getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.people_recycler_view);

//
        mRecyclerView.setHasFixedSize(true);
//
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
//
        String[] podaci = {"Emili","Goca","Aleksa", "Zoki"};
        mAdapter = new PeopleDataAdapter(podaci,context);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
