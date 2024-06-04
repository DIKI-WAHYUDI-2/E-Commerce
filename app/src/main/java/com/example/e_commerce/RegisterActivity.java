package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.e_commerce.data.response.User;
import com.example.e_commerce.data.response.UserRequest;
import com.example.e_commerce.data.retrofit.ApiConfig;
import com.example.e_commerce.data.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name,email,password;
    private Button signUp;
    private LinearLayout alreadyHaveAccount;
    private ApiService config;
    private ProgressBar progressBar;
    private ImageView vector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.ProgressBar01);
        vector = findViewById(R.id.vector);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    vector.setVisibility(View.VISIBLE);
                } else {
                    vector.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        config = ApiConfig.getApiService();
        signUp.setOnClickListener(view -> {
            String nameValue = name.getText().toString();
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            createUser(nameValue,emailValue,passwordValue);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            name.setText("");
            email.setText("");
            password.setText("");
        });

        alreadyHaveAccount.setOnClickListener(click -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void createUser(String name,String email,String password){
        showLoading(true);
        UserRequest request = new UserRequest(name,email,password);
        Call<User> call = config.createUser(request);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showLoading(false);
                if (response.isSuccessful()){
                    User user = response.body();
                    Log.d("RegisterActivity", "User created: " + request.getname());
                }else {
                    Log.d("RegisterActivity", "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showLoading(false);
                Log.e("MainActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}