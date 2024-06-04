package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccesShopping extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_1);

        Button button = findViewById(R.id.btnContinueShopping);
        button.setOnClickListener(v->{
            Intent intent = new Intent(SuccesShopping.this, Container.class);
            startActivity(intent);
        });
    }
}
