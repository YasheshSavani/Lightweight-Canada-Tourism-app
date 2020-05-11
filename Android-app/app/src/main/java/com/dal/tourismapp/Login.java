package com.dal.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    Button signup;
    Button login;
    TextInputEditText Email;
    TextInputEditText Password;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.Login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = Email.getText().toString();
                password = Password.getText().toString();
                if (email != "" && password != ""){
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Login( email, password);
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void Login(final String email, String password) {

        try {
            String URL = "http://52.70.2.41:5002/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("UserEmail", email);
                    ed.apply();

                    Intent intent = new Intent(getApplicationContext(), OTP.class);
                    try {
                        intent.putExtra("otp", response.getString("otp"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    finish();
                    System.out.println(response);
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
            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
