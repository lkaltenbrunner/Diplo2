package com.example.kalti.diplo.WeatherFragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kalti.diplo.R;
import com.example.kalti.diplo.WeatherActivity;
import com.example.kalti.diplo.Weather_Models.WeatherItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Kalti on 04.03.2018.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback{



    GoogleMap mMap;
    MarkerOptions markerOptions;
    Button btnlocation;
    WeatherFragment weatherFragment;
    private String coord;

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    double lat, lon;
    double currentlat, currentlon;

    public double getCurrentlat() {
        return currentlat;
    }

    public void setCurrentlat(double currentlat) {
        this.currentlat = currentlat;
    }

    public double getCurrentlon() {
        return currentlon;
    }

    public void setCurrentlon(double currentlon) {
        this.currentlon = currentlon;
    }

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*
       View view = inflater.inflate(R.layout.maps_fragment,container,false);
        supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapselectioncoor);
        if(supportMapFragment == null){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapselectioncoor, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        return view;
        */


        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.changelocationbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WeatherActivity.SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("coords",coord);
                editor.commit();


                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();

                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);
/*
                weatherFragment = new WeatherFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.weatherframe,weatherFragment);
                fragmentTransaction.commit();
                */

            }
        });
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapselectioncoor);
        supportMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        LatLng currentLatLng = new LatLng(currentlat,currentlon);
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        coord = "lat=" +currentlat+ "&lon="+currentlon;


        //Toast.makeText(getActivity(),coord,Toast.LENGTH_SHORT).show();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                currentlat= latLng.latitude;
                currentlon = latLng.longitude;
                LatLng selectedLatLng = new LatLng(currentlat,currentlon);
                markerOptions = new MarkerOptions().position(selectedLatLng).title("Selected Location");
                coord = "lat=" +currentlat+ "&lon="+currentlon;

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
                //Toast.makeText(getActivity(),coord,Toast.LENGTH_SHORT).show();


            }
        });


    }





}
