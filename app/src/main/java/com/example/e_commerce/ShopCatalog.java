package com.example.e_commerce;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.adapter.AdapterShopCatalog;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopCatalog extends AppCompatActivity {

    private String NAME_CATEGORY = "NAME_CATEGORY";
    private String NAME_SUB_CATEGORY = "NAME_SUB_CATEGORY";
    private String email,password;
    private String productCategory,productSubCategory;
    private RecyclerView recyclerView;
    private TextView subCategoryTitle;
    private AdapterShopCatalog adapterShopCatalog;
    private List<Product> product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_catalog);

        product = new ArrayList<>();
        subCategoryTitle = findViewById(R.id.titleSubCategory);
        recyclerView = findViewById(R.id.recycleViewCatalog);
        adapterShopCatalog = new AdapterShopCatalog(product);

        recyclerView.setAdapter(adapterShopCatalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        email = "dikiwahyudi@gmail.com";
        password = "123456";

        if (getIntent().hasExtra("NAME_CATEGORY") && getIntent().hasExtra("NAME_SUB_CATEGORY")){
            productCategory = getIntent().getStringExtra(NAME_CATEGORY);
            productSubCategory = getIntent().getStringExtra(NAME_SUB_CATEGORY);
            subCategoryTitle.setText(getIntent().getStringExtra(NAME_SUB_CATEGORY));
            getAllProductByCategory(productCategory,productSubCategory);
        }

    }

    private void getAllProductByCategory(String productCategory,String productSubCategory){
        ApiService config = ApiConfigAuthenticated.getApiService(email,password);
        Call<ProductResponse> call = config.getProductsByCategoryAndSubCategory(productCategory,productSubCategory);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null){
                    List<Product> products = response.body().getData();
                    Log.d("ShopCatalog", "Successfully fetching data");
                    if (!products.isEmpty()){
                       AdapterShopCatalog adapterShopCatalog =  new AdapterShopCatalog(products);
                       recyclerView.setAdapter(adapterShopCatalog);

                    }
                }else {
                    Log.d("ShopCatalog", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("ShopCatalog", "Failed fetch data " + t.getMessage());
            }
        });
    }
}
