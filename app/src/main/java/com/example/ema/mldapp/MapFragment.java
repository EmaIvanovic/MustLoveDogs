package com.example.ema.mldapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double m_radius = 0;
    private boolean friends_enabled = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_map, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRad = getActivity().findViewById(R.id.btnRadius);
        btnRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnRadius)
                {
                    EditText t = getActivity().findViewById(R.id.txtRadius);
                    m_radius = Double.parseDouble(t.getText().toString());
                    AddPostMarkers(RadiusSearch(m_radius));
                }
            }
        });

        Button btnFred = getActivity().findViewById(R.id.btnMapFriends);
        btnFred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnMapFriends)
                {
                    Button btn = (Button) v;
                    if(friends_enabled){

                        friends_enabled = false;
                        btn.setText("Show friends");

                        mMap.clear();

                        SetMainMarker();

                        if(m_radius == 0)
                            AddPostMarkers(HomeActivity.friendPosts);
                        else
                            AddPostMarkers(RadiusSearch(m_radius));
                    }
                    else{

                        friends_enabled = true;
                        btn.setText("Hide friends");
                        AddFriendMarkers();
                    }

                }
            }
        });

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(marker.getSnippet().equals(""))
                {
                    Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                    intent.putExtra("username", marker.getTitle());
                    getActivity().startActivity(intent);
                }
            }
        });

        AddPostMarkers(HomeActivity.friendPosts);

        SetMainMarker();
    }

    void SetMainMarker(){

        HomeActivity.currentUser.setLat("-34");
        HomeActivity.currentUser.setLng("151");

        LatLng userLoc = new LatLng(Double.parseDouble(HomeActivity.currentUser.getLat()), Double.parseDouble(HomeActivity.currentUser.getLng()));

        mMap.addMarker(new MarkerOptions()
                .position(userLoc)
                .title("You are here !!!")
                .snippet("This is your current location.")
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 12));
    }

    ArrayList<Post> RadiusSearch(double radius){

        ArrayList<Post> wantedPosts = new ArrayList<Post>();
        LatLng u = new LatLng(Double.parseDouble(HomeActivity.currentUser.getLat()), Double.parseDouble(HomeActivity.currentUser.getLng()));

        for(int i = 0; i < HomeActivity.friendPosts.size(); i++){

            LatLng p = new LatLng(Double.parseDouble(HomeActivity.friendPosts.get(i).getLat()), Double.parseDouble(HomeActivity.friendPosts.get(i).getLng()));

            float[] dist = new float[3];
            Location.distanceBetween(u.latitude, u.longitude, p.latitude, p.longitude, dist);
            double distance = dist[0]/1000;

            if(radius > distance)
                wantedPosts.add(HomeActivity.friendPosts.get(i));
        }

        return wantedPosts;
    }



    void AddPostMarkers(ArrayList<Post> p) {
        mMap.clear();

        SetMainMarker();

        for(int i = 0; i < p.size(); i++){

            LatLng postLoc = new LatLng(Double.parseDouble(p.get(i).getLat()), Double.parseDouble(p.get(i).getLng()));
            mMap.addMarker(new MarkerOptions()
                    .position(postLoc)
                    .title(p.get(i).getTypeOfPost())
                    .snippet(p.get(i).getDesc())
            ).setTag(p.get(i));
        }
    }

    void AddFriendMarkers(){

        for(int i = 0; i < HomeActivity.friendNicks.size(); i++){

            LatLng postLoc = new LatLng(-34 - ( + 1)*0.2, 151 - (i + 1)*0.15);
            mMap.addMarker(new MarkerOptions()
                    .position(postLoc)
                    .title(HomeActivity.friendNicks.get(i))
                    .snippet("")
            );
        }
    }

}
