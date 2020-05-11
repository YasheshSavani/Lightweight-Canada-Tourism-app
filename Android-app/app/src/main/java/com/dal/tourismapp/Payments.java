package com.dal.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Payments extends AppCompatActivity {

    Button date;
    String dateString;
    TextView dateToDisplay;
    Button confirmBooking;
    TextInputEditText etSource;
    TextInputEditText etNumberOfPassengers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        date = findViewById(R.id.date);
        confirmBooking = findViewById(R.id.book);
        etSource = findViewById(R.id.source);
        etNumberOfPassengers = findViewById(R.id.numberOfPeople);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker picker = builder.build();


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getSupportFragmentManager(), picker.toString());

            }
        });
        picker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("DatePicker Activity", "Dialog was cancelled");
            }
        });
        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
            }
        });
       picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
           @Override
           public void onPositiveButtonClick(Object selection) {
              dateString= picker.getHeaderText();
               date.setText(dateString);
           }
       });

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel("Purchase")
                .setup(this);

        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        booking();
                    }
                };

                //retrieve data on the Thread.
                Thread thread = new Thread(null, runnable, "background");
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), Bookings.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void booking(){

            SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
            String user_email = sp.getString("UserEmail", null);
            System.out.println(user_email);

            try {
                String URL = "http://52.70.2.41:5001/ticketcreation";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", "akash80ef@gmail.com");
                jsonBody.put("source", etSource.getText().toString());
                jsonBody.put("date_travel", dateString);
                jsonBody.put("destination_name", "aa");
                jsonBody.put("city", "aa");
                jsonBody.put("province", "aa");
                jsonBody.put("ticket_price", "1");
                jsonBody.put("passenger_number", etNumberOfPassengers.getText().toString());
                jsonBody.put("busid", "aa");

                JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        //onBackPressed();

                    }
                });
                RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

        }




}
