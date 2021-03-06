package com.example.tria_rozenberg_hw2;

import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {

    // keys and addresses
    // google request format: https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
    final String googleAddress = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    final String googleKey = "&key=AIzaSyCNND_DqC1ODB6J5a5K3W_IYQRWJAL9Qz4";
    final String darkSkyAddress = "https://api.darksky.net/forecast/fb4ffcd5b6e8f190449448adc636025a/";

    public MapView mapView;
    public GoogleMap googMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing default map
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                moveMap(googleMap, 30.3, -97.7);
            }
        });
    }

    // Function to recenter map on input coordinates
    public void moveMap(GoogleMap gMap, double lat, double lng){
        googMap = gMap;
        LatLng coord = new LatLng(lat,lng);
        googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 16));
        googMap.addMarker(new MarkerOptions().position(coord));
    }

    // Required functions to implement Map
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle bndl){
        super.onSaveInstanceState(bndl);
        mapView.onSaveInstanceState(bndl);
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }


    // Function that starts the process
    public void onSubmit(View view) {
        String address = getAddress();
        callGoogle(googleAddress + address + googleKey);
    }

    // Hadar driving
    // Get address from input and reformat for GoogleMap
    public String getAddress() {
        EditText inpt = (EditText) findViewById(R.id.editText);
        String address = inpt.getText().toString();
        address = address.replace(" ", "+");
        return address;
    }

    // Sends address to Google API
    public void callGoogle(final String address) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, address, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                readCoords(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest); // Adds JSONRequest to queue
    }

    // Lorrie driving
    // Reading the coordinates and sending to DarkSky and Google
    public void readCoords(JSONObject rspns) {
        TextView txtv = (TextView) findViewById(R.id.textView);

        // default
        double lng = -97.7;
        double lat = 30.3;

        try {
            JSONObject temp = rspns.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            lng = temp.getDouble("lng");
            lat = temp.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String coord = Double.toString(lat) + "," + Double.toString(lng);
        callDarkSky(coord);
        moveMap(googMap, lat, lng);
    }

    // Function that retrieves weather conditions from DarkSky
    public void callDarkSky(String coord) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, darkSkyAddress + coord, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Condition: temperature, humidity, wind speed, precipitation
                try {
                    String temp = Double.toString(response.getJSONObject("currently").getDouble("temperature"));
                    String humid = Double.toString(response.getJSONObject("currently").getDouble("humidity"));
                    String windspeed = Double.toString(response.getJSONObject("currently").getDouble("windSpeed"));
                    double precip_prob_num = response.getJSONObject("currently").getDouble("precipProbability");
                    String precip_type = null;

                    // Checking if type of precipitation exists
                    if (precip_prob_num != 0) {
                        precip_type = response.getJSONObject("currently").getString("precipType");
                    }

                    String precip_prob = Double.toString(precip_prob_num);
                    TextView textView = (TextView) findViewById(R.id.textView);

                    if (precip_type == null) {
                        textView.setText("The temperature is " + temp + "\nThe humidity is " + humid + "\nThe wind speed is " + windspeed + "\nThe precipitation probability is " + precip_prob);
                    } else
                        textView.setText("The temperature is " + temp + "\nThe humidity is " + humid + "\nThe wind speed is " + windspeed + "\nThe precipitation probability is " + precip_prob + "\nThe precipitation type is " + precip_type);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);    // Adding JSONRequest to queue
    }
}

