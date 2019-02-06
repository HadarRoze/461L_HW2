package com.example.tria_rozenberg_hw2;

import android.app.DownloadManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
// keys and addresses
    //google request format: https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
    final String googleAddress = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    final String googleKey = "&key=AIzaSyCNND_DqC1ODB6J5a5K3W_IYQRWJAL9Qz4";
    final String darkSkyAddress = "https://api.darksky.net/forecast/fb4ffcd5b6e8f190449448adc636025a/";
    public JSONArray rspns;


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
        String address = getAddress();
        Log.i("tag","Passed get address");
        TextView txtv = (TextView) findViewById(R.id.textView);
        txtv.setText(googleAddress+address+googleKey);
        callGoogle(googleAddress+address+googleKey);
        //readCoords();
        //TextView txtv = (TextView) findViewById(R.id.textView);
        //txtv.setText(rspns.toString());
    }

    //Hadar driving
    public String getAddress(){
        EditText inpt = (EditText) findViewById(R.id.editText);
        String address = inpt.getText().toString();
        address = address.replace(" ", "+");
        return address;
    }

    public void callGoogle(final String address){
        Log.i("tag","Got to call google");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, address, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("tag","Got response");
                TextView txtv = (TextView) findViewById(R.id.textView);
                txtv.setText(address);
                //rspns = response;
            }
        }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void readCoords(){
        TextView txtv = (TextView) findViewById(R.id.textView);
        JSONObject temp = null;
        String lng = null;
        String lat = null;
        /*try {
            temp = rspns.getJSONObject("geometry").getJSONObject("location");
            lng = Double.toString(temp.getDouble("lng"));
            lat = Double.toString(temp.getDouble("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        txtv.setText(lng+" "+lat);
    }

    }
