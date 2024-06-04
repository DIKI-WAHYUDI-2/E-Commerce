package com.example.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.data.response.Image;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProduct extends AppCompatActivity {

    private EditText name, brand, price, description;
    private Spinner category, subcategory;
    private Button selectImage, add;
    private ProgressBar progressBar;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Integer[] valueCategory = {1, 2, 3};
    private Integer[] valueSubCategory = {1, 2, 3, 4, 5};
    private ImageView image;
    private Uri imageUri;
    private String PRODUCT_NAME = "PRODUCT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_product);

        name = findViewById(R.id.etNameProductUpdate);
        brand = findViewById(R.id.etBrandProductUpdate);
        price = findViewById(R.id.etPriceProductUpdate);
        description = findViewById(R.id.etDeskripsiProductUpdate);
        category = findViewById(R.id.spinnerCategoryUpdate);
        subcategory = findViewById(R.id.spinnerSubCategoryUpdate);
        selectImage = findViewById(R.id.buttonSelectImageUpdate);
        add = findViewById(R.id.btnUpdateProduct);
        image = findViewById(R.id.imageViewUpdateProduct);
        progressBar = findViewById(R.id.ProgressUpdateProduct);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                Integer value = valueCategory[position];
                Toast.makeText(parent.getContext(), "Selected: " + category + " with value: " + value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        ArrayAdapter<CharSequence> subcategoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.subcategory_array, android.R.layout.simple_spinner_item);
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcategory.setAdapter(subcategoryAdapter);

        subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subcategory = parent.getItemAtPosition(position).toString();
                Integer value = valueSubCategory[position];
                Toast.makeText(parent.getContext(), "Selected: " + subcategory + " with value: " + value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        selectImage.setOnClickListener(v -> openGallery());

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        image.setImageURI(imageUri);
                        Toast.makeText(this, "Image Selected: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_NAME")) {
            PRODUCT_NAME = intent.getStringExtra("PRODUCT_NAME");
        }

        getProductByName();
    }

    private void updateProduct(Integer id,String name, String brand, Integer price, String description, Integer category, Integer subCategory){
        ApiService config = ApiConfigAuthenticated.getApiService("dikiwahyudi@gmail.com", "123456");

        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), id.toString());
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody brandBody = RequestBody.create(MediaType.parse("text/plain"), brand);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price.toString());
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category.toString());
        RequestBody subCategoryBody = RequestBody.create(MediaType.parse("text/plain"), subCategory.toString());

        MultipartBody.Part imageBody = null;
        if (imageUri != null) {
            File imageFile = new File(CreateProduct.FileUtils.getPath(getApplicationContext(), imageUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), imageFile);
            imageBody = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        }

        Call<Product> call = config.updateProduct(idBody,nameBody, priceBody, brandBody, descriptionBody, categoryBody, subCategoryBody, imageBody);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    Toast.makeText(UpdateProduct.this, "Product updated: " + nameBody, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(UpdateProduct.this, "Failed to updated product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(UpdateProduct.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductByName(){
        showLoading(true);
        ApiService config = ApiConfigAuthenticated.getApiService("dikiwahyudi@gmail.com","123456");
        Call<ProductResponse> call = config.getProductByName(PRODUCT_NAME);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Product> products = response.body().getData();
                    Log.d("UpdateProduct", "Successfully fetching data");
                    if (!products.isEmpty()) {
                        Product product = products.get(0);
                        Integer id = product.getId();
                        displayProductDetails(product);
                        add.setOnClickListener(v->{
                            String nameText = name.getText().toString();
                            String brandText = brand.getText().toString();
                            Integer priceValue = Integer.parseInt(price.getText().toString());
                            String descriptionText = description.getText().toString();
                            Integer categoryValue = valueCategory[category.getSelectedItemPosition()];
                            Integer subCategoryValue = valueSubCategory[subcategory.getSelectedItemPosition()];

                            updateProduct(id, nameText, brandText, priceValue, descriptionText, categoryValue, subCategoryValue);
                        });
                    }
                } else {
                    Log.d("UpdateProduct", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                showLoading(false);
                Log.d("UpdateProduct", "Failed fetch data " + t.getMessage());
            }
        });
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void displayProductDetails(Product product) {
        name.setText(product.getName());
        price.setText("Rp " + product.getPrice());
        brand.setText(product.getBrand());
        description.setText(product.getDescription());

        List<Image> images = product.getImage();
        for (int i = 0; i < images.size(); i++) {
            Bitmap bitmap = decodeBase64ToBitmap(images.get(i).getImageUrl());
            if (i == 0) {
                image.setImageBitmap(bitmap);
            }
        }
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    public static class FileUtils {
        public static String getPath(Context context, Uri uri) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();
                return path;
            }
            return null;
        }
    }
}
