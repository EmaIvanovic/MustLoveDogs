package com.example.ema.mldapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .snippet("This is starting marker. Very nice info included. Lost dog, description, all that stuff.")
        );

        for(int i = 0; i < HomeActivity.posts.size(); i++){

            LatLng postLoc = new LatLng(Double.parseDouble(HomeActivity.posts.get(i).getLat()), Double.parseDouble(HomeActivity.posts.get(i).getLng()));
            mMap.addMarker(new MarkerOptions()
                    .position(postLoc)
                    .title(HomeActivity.posts.get(i).getTypeOfPost())
                    .snippet(HomeActivity.posts.get(i).getDesc())
            ).setTag(HomeActivity.posts.get(i));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
