package com.example.shortestpath;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.shortestpath.libs.TaskLoadedCallback;
import com.example.shortestpath.libs.FetchURL;


import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
    Button getDirection;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    //Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place1 = new MarkerOptions().position(new LatLng(13.692035,100.6451767)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(13.752356, 100.629911)).title("Location 2");

        getDirection = findViewById(R.id.buttonGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new FetchURL(MainActivity.this).execute(getDirectionsUrl(place1.getPosition(), place2.getPosition()), "driving");
            }
        });
        //Test Coordinates
        //Place1 13.692035,100.6451767
        //Place2 13.752356, 100.629911
    }

    //OnMap interactive
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        mMap.addMarker(place2);
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination){
        //Settings
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of destination
        String str_des = "destination=" + destination.latitude + "," + destination.longitude;
        //Set Value enable Sensor
        String sensor = "sensor=false";
        //Set Value of mode
        String mode = "mode=driving";
        //Build full param
        String param = str_org + "&" + str_des + "&" + sensor + "&" + mode;
        //Output format
        String output = "json";
        //request URL
        String url = "https://map.googleapis.com/maps/directions/" + output + "?" + param;
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}



