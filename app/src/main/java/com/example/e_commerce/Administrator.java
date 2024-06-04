package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Administrator extends AppCompatActivity {

    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        cardView = findViewById(R.id.cdAdministratorProduct);
        cardView.setOnClickListener(v->{
            Intent intent = new Intent(Administrator.this, AdminProduct.class);
            startActivity(intent);
        });
    }
}
