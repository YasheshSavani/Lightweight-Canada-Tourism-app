package com.dal.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class Search extends AppCompatActivity {
    Button bSearch;
    TextInputEditText etKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bSearch = findViewById(R.id.searchButton);
        etKeyword = findViewById(R.id.keywordField);
        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        String user_email = sp.getString("UserEmail", null);
        System.out.println("akash" +  user_email);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = etKeyword.getText().toString();
                Intent intent = new Intent(getApplicationContext(), PlacesList.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);

            }
        });
    }
}
