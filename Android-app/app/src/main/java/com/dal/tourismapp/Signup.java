package com.dal.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity {
    TextInputEditText Name;
    TextInputEditText Email;
    TextInputEditText Password;
    Button signup;
    String email;
    String name;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        System.out.println(email);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name = Name.getText().toString();
                 email = Email.getText().toString();
                 password = Password.getText().toString();
                if (!name.equals("") && !email.equals("") && !password.equals("")){
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            SignUp(name, email, password);
                        }
                    };

                    //retrieve data on the Thread.
                    Thread thread = new Thread(null, runnable, "background");
                    thread.start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter the required fields",Toast.LENGTH_SHORT ).show();
                }
            }
        });


    }
    public void SignUp(String name, String email, String password) {

        try {
            String URL = "http://52.70.2.41:5002/signup";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            System.out.println(email);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(), "Signup Successful " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    Toast.makeText(getApplicationContext(), "Signup Successful " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    //onBackPressed();

                }
            });
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObject);

        } catch (JSONException e) {

        }
    }
}
