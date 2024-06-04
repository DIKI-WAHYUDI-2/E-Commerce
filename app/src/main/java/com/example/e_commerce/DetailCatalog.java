package com.example.e_commerce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.data.response.Image;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;
import com.example.e_commerce.fragment.bag.BagFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCatalog extends AppCompatActivity {

    private String productName;
    private String email, password;
    private String NAME_PRODUCT = "NAME_PRODUCT";
    private TextView tvName, tvBrand, tvPrice, tvDescription;
    private ImageView imgProduct1, imgProduct2, imgProduct3;
    private Button btnDetail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_catalog);

        tvName = findViewById(R.id.detailName);
        tvBrand = findViewById(R.id.detailBrand);
        tvPrice = findViewById(R.id.detailPrice);
        tvDescription = findViewById(R.id.detailDescription);
        imgProduct1 = findViewById(R.id.detailImg);
        imgProduct2 = findViewById(R.id.detailImg2);
        imgProduct3 = findViewById(R.id.detailImg3);
        btnDetail = findViewById(R.id.btnDetail);
        progressBar = findViewById(R.id.ProgressBarDetail);

        email = "dikiwahyudi@gmail.com";
        password = "123456";

        if (getIntent().hasExtra("NAME_PRODUCT")) {
            productName = getIntent().getStringExtra(NAME_PRODUCT);
            getDetailProductByName(productName);
        }
    }

    private void getDetailProductByName(String nameProduct) {
        showLoading(true);
        ApiService config = ApiConfigAuthenticated.getApiService(email, password);
        Call<ProductResponse> call = config.getProductByName(nameProduct);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Product> products = response.body().getData();
                    Log.d("DetailCatalog", "Successfully fetching data");
                    if (!products.isEmpty()) {
                        Product product = products.get(0);
                        displayProductDetails(product);
                    }
                } else {
                    Log.d("DetailCatalog", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                showLoading(false);
                Log.d("DetailCatalog", "Failed fetch data " + t.getMessage());
            }
        });
    }

    private void sendProductToCart(Integer id){
        ApiService config = ApiConfigAuthenticated.getApiService(email,password);
        Call<ProductResponse> call = config.sendProductToCart(id);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null){
                    List<Product> products = response.body().getData();
                    Product product = products.get(0);
                    Snackbar.make(findViewById(android.R.id.content), "Success add product to cart", Snackbar.LENGTH_LONG).show();
                    Log.d("DetailCatalog", "Success add product to cart with product id: " + product.getId());

                    BagFragment bagFragment = (BagFragment) getSupportFragmentManager().findFragmentByTag("BagFragment");
                    if (bagFragment != null) {
                        bagFragment.updateProductList(products);
                    }
                }else {
                    Log.d("DetailCatalog", "Response unsuccessful or body is null");
                    Snackbar.make(findViewById(android.R.id.content), "Product already in cart", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("DetailCatalog", "Failed fetch data " + t.getMessage());
            }
        });
    }



    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void displayProductDetails(Product product) {
        tvName.setText(product.getName());
        tvPrice.setText("Rp " + product.getPrice());
        tvBrand.setText(product.getBrand());
        tvDescription.setText(product.getDescription());

        List<Image> images = product.getImage();
        for (int i = 0; i < images.size(); i++) {
            Bitmap bitmap = decodeBase64ToBitmap(images.get(i).getImageUrl());
            if (i == 0) {
                imgProduct1.setImageBitmap(bitmap);
            } else if (i == 1) {
                imgProduct2.setImageBitmap(bitmap);
            } else if (i == 2){
                imgProduct3.setImageBitmap(bitmap);
            }
        }

        btnDetail.setOnClickListener(click -> {
            sendProductToCart(product.getId());
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
