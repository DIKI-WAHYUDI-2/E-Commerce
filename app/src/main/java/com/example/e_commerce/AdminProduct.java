package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.adapter.AdapterProductAdmin;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterProductAdmin adapterProductAdmin;
    private ImageView imageView;
    private List<Product> products;
    public static final int UPDATE_PRODUCT_REQUEST_CODE = 1001;
    private final ActivityResultLauncher<Intent> addProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    getProducts();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        products = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleViewLOP);
        imageView = findViewById(R.id.addProductByAdmin);
        adapterProductAdmin = new AdapterProductAdmin(products);
        recyclerView.setAdapter(adapterProductAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageView.setOnClickListener(v->{
            Intent intent = new Intent(AdminProduct.this, CreateProduct.class);
            addProductLauncher.launch(intent);
        });

        getProducts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_PRODUCT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getProducts();
            }
        }
    }

    private void getProducts() {
        ApiService apiService = ApiConfigAuthenticated.getApiService("dikiwahyudi@gmail.com", "123456");
        Call<ProductResponse> call = apiService.getProduct();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    products.clear();
                    products.addAll(response.body().getData());
                    adapterProductAdmin.notifyDataSetChanged();
                } else {
                    Log.d("MainPage ViewModel", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("AdminProduct", "Failed fetch data error: " + t.getMessage());
            }
        });

    }
}
