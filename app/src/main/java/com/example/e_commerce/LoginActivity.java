package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.e_commerce.data.response.LoginUserRequest;
import com.example.e_commerce.data.response.User;
import com.example.e_commerce.data.retrofit.ApiConfig;
import com.example.e_commerce.data.retrofit.ApiService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEt,passwordEt;
    private Button login;
    private ApiService config;
    private ProgressBar progressBar;
    private TextView showLoginFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.etEmailLogin);
        passwordEt = findViewById(R.id.etPasswordLogin);
        login = findViewById(R.id.btnlogin);
        progressBar = findViewById(R.id.ProgressBar02);
        showLoginFailed = findViewById(R.id.failedLogin);
        config = ApiConfig.getApiService();

        login.setOnClickListener(click -> {
            String emailValue = emailEt.getText().toString();
            String passwordValue = passwordEt.getText().toString();
            loginUser(emailValue,passwordValue);
        });
    }

    private void loginUser(String email,String password){
        showLoading(true);
        LoginUserRequest request = new LoginUserRequest(email,password);
        Call<User> call = config.loginUser(request);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showLoading(false);
                if (response.isSuccessful()){
                    User user = response.body();
                    showLoginFailed.setText("");

                    SharedPreferences sharedPreferences =  getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",email);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, Container.class);
                    startActivity(intent);

                    emailEt.setText("");
                    passwordEt.setText("");
                    Snackbar.make(findViewById(android.R.id.content), "Login Successfully", Snackbar.LENGTH_LONG).show();
                    Log.d("LoginActivity", "User logged in successfully: " + request.getEmail());
                }else {
                    showLoginFailed.setText("Email or Password is incorrect");
                    Log.d("LoginActivity", "User logged in failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showLoading(false);
                Log.e("LoginActivity", "Error: " + t.getMessage());
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