package com.example.ema.mldapp;

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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

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
                    double m_radius = Double.parseDouble(t.getText().toString());
                    AddPostMarkers(RadiusSearch(m_radius));
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

        AddPostMarkers(HomeActivity.posts);

        // Add a marker on user's location and move the camera
        SetMainMarker();
    }

    void SetMainMarker(){
        LatLng sydney = new LatLng(-34, 151);

        HomeActivity.currentUser.setLat("-34");
        HomeActivity.currentUser.setLng("151");

        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .snippet("This is starting marker. Very nice info included. Lost dog, description, all that stuff.")
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
    }

    ArrayList<Post> RadiusSearch(double radius){

        ArrayList<Post> wantedPosts = new ArrayList<Post>();
        LatLng u = new LatLng(Double.parseDouble(HomeActivity.currentUser.getLat()), Double.parseDouble(HomeActivity.currentUser.getLng()));

        for(int i = 0; i < HomeActivity.posts.size(); i++){

            LatLng p = new LatLng(Double.parseDouble(HomeActivity.posts.get(i).getLat()), Double.parseDouble(HomeActivity.posts.get(i).getLng()));

            float[] dist = new float[3];
            Location.distanceBetween(u.latitude, u.longitude, p.latitude, p.longitude, dist);
            double distance = dist[0]/1000;

            if(radius > distance)
                wantedPosts.add(HomeActivity.posts.get(i));
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
}
