package com.dal.tourismapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Bookings extends AppCompatActivity {
    private RecyclerView recycle_view;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> place;
    private ArrayList<String> city;
    private ArrayList<String> ticket_id;
    private ArrayList<String> people;
    private ArrayList<String> total;
    private ArrayList<String> date_travel;
    private ArrayList<String> date_booked;
    private ArrayList<String> source;
    private JSONArray placesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

         recycle_view = findViewById(R.id.booking_list);
        recycle_view.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(layoutManager);

        place = new ArrayList<>();
        city = new ArrayList<>();
        ticket_id = new ArrayList<>();
        people = new ArrayList<>();
        total = new ArrayList<>();
        date_booked = new ArrayList<>();
        date_travel = new ArrayList<>();
        source = new ArrayList<>();
        placesArray = new JSONArray();
        final String email = getIntent().getStringExtra("email");

        //create a thread for fetching jokes
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getBooking();
            }
        };

        //retrieve data on the Thread.
        Thread thread = new Thread(null, runnable, "background");
        thread.start();

        mAdapter = new BookingAdapter(place, city, ticket_id, people, source, date_booked, date_travel, total, getApplicationContext());
        recycle_view.setAdapter(mAdapter);
    }

   public void getBooking(String email){
       String url = "http://52.70.2.41:5001/gettickets?email=" + email ;

       JsonObjectRequest request = new JsonObjectRequest(
               Request.Method.GET, url, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {

                       try {
                           System.out.println(response);
                           placesArray = response.getJSONArray("Result");
                           for(int i=0;i<placesArray.length();i++){
                               JSONObject placeObject = (JSONObject) placesArray.get(i);
                               place.add(placeObject.getString("Destination_name"));
                               city.add(placeObject.getString("City"));
                               ticket_id.add(placeObject.getString("Ticketid"));
                               people.add(placeObject.getString("Passenger_number"));
                               source.add(placeObject.getString("Source"));
                               date_booked.add(placeObject.getString("Date_booked"));
                               date_travel.add(placeObject.getString("Date_travel"));
                               total.add(placeObject.getString("Total"));
                           }
                           //notify to refresh the view related to the dataSet
                           mAdapter.notifyDataSetChanged();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError e) {
               e.printStackTrace();
               Toast.makeText(getApplicationContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
           }
       }
       );

       //adding the request to the request Queue
       RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
