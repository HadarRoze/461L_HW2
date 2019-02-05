package com.example.tria_rozenberg_hw2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
// keys and addresses
    //google request format: https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
    final String googleAddress = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    final String googleKey = "AIzaSyCNND_DqC1ODB6J5a5K3W_IYQRWJAL9Qz4";
    final String darkSkyAddress = "https://api.darksky.net/forecast/fb4ffcd5b6e8f190449448adc636025a/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 1. Read the input address and convert it to google address
    // 2. Send that call to the google API
    // 3. Read the coordinates and send them to darkSky API
    // 4. Display the weather conditions (temperature, humidity, wind speed, precipitation)
    // 5. Display on map
    public void onSubmit(View view) {

    }

    
    public String getAddress(){
        EditText inpt = (EditText) findViewById(R.id.editText);



    }

    }
