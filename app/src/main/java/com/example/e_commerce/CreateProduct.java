package com.example.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProduct extends AppCompatActivity {

    private EditText name, brand, price, description;
    private Spinner category, subcategory;
    private Button selectImage, add;
    private ProgressBar progressBar;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Integer[] valueCategory = {1, 2, 3};
    private Integer[] valueSubCategory = {1, 2, 3, 4, 5};
    private ImageView image;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        name = findViewById(R.id.etNameProduct);
        brand = findViewById(R.id.etBrandProduct);
        price = findViewById(R.id.etPriceProduct);
        description = findViewById(R.id.etDeskripsiProduct);
        category = findViewById(R.id.spinnerCategory);
        subcategory = findViewById(R.id.spinnerSubCategory);
        selectImage = findViewById(R.id.buttonSelectImage);
        add = findViewById(R.id.btnAddProduct);
        image = findViewById(R.id.imageViewAddProduct);
        progressBar = findViewById(R.id.ProgressCreateProduct);

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

        add.setOnClickListener(v -> {
            String nameText = name.getText().toString();
            String brandText = brand.getText().toString();
            Integer priceValue = Integer.parseInt(price.getText().toString());
            String descriptionText = description.getText().toString();
            Integer categoryValue = valueCategory[category.getSelectedItemPosition()];
            Integer subCategoryValue = valueSubCategory[subcategory.getSelectedItemPosition()];

            createProduct(nameText, brandText, priceValue, descriptionText, categoryValue, subCategoryValue);
        });
    }

    private void createProduct(String name, String brand, Integer price, String description, Integer category, Integer subCategory) {
        ApiService config = ApiConfigAuthenticated.getApiService("dikiwahyudi@gmail.com", "123456");

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody brandBody = RequestBody.create(MediaType.parse("text/plain"), brand);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price.toString());
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category.toString());
        RequestBody subCategoryBody = RequestBody.create(MediaType.parse("text/plain"), subCategory.toString());

        MultipartBody.Part imageBody = null;
        if (imageUri != null) {
            File imageFile = new File(FileUtils.getPath(getApplicationContext(), imageUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), imageFile);
            imageBody = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        }

        Call<Product> call = config.createProduct(nameBody, priceBody, brandBody, descriptionBody, categoryBody, subCategoryBody, imageBody);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    Toast.makeText(CreateProduct.this, "Product created: " + nameBody, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Set result code to indicate success
                    finish();
                } else {
                    Toast.makeText(CreateProduct.this, "Failed to create product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(CreateProduct.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
