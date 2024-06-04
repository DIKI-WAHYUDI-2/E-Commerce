package com.example.e_commerce.data.retrofit;

import com.example.e_commerce.data.response.CategoriesResponse;
import com.example.e_commerce.data.response.LoginUserRequest;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.response.SessionResponse;
import com.example.e_commerce.data.response.SubCategoriesResponse;
import com.example.e_commerce.data.response.User;
import com.example.e_commerce.data.response.UserRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("users")
    Call<User> createUser(@Body UserRequest request);
    @POST("auth/login")
    Call<User> loginUser(@Body LoginUserRequest request);
    @Multipart
    @POST("products")
    Call<Product> createProduct(
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("brand") RequestBody brand,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part("subcategory") RequestBody subcategory,
            @Part MultipartBody.Part image
            );
    @Multipart
    @PUT("users/user")
    Call<User> addImage(@Query("email") String email, @Part MultipartBody.Part image);
    @Multipart
    @PUT("products/product-by")
    Call<Product> updateProduct(
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("brand") RequestBody brand,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part("subcategory") RequestBody subcategory,
            @Part MultipartBody.Part image
    );
    @PUT("products/product")
    Call<ProductResponse> sendProductToCart(@Query("id") Integer id);
    @GET("products")
    Call<ProductResponse> getProduct();
    @GET("products/product-order-by")
    Call<ProductResponse> getAllProductOrderByNameDes();
    @GET("users/session")
    Call<SessionResponse> getSessionByUser(@Query("email") String email);
    @GET("categories")
    Call<CategoriesResponse> getCategory();
    @GET("products/product")
    Call<ProductResponse> getProductByName(@Query("name") String productName);
    @GET("products/category")
    Call<ProductResponse> getAllProductByCategory(@Query("name")String productCategory);
    @GET("subcategories/category")
    Call<SubCategoriesResponse> getSubCategorY(@Query("name") String category);
    @GET("products/cart")
    Call<ProductResponse> getAllDataByCart();
    @GET("products/category-and-subcategory")
    Call<ProductResponse> getProductsByCategoryAndSubCategory(@Query("category") String category, @Query("subCategory") String subCategory);
    @DELETE("products/product")
    Call<Void> removeProductFromCart(@Query("id") Integer id);
    @DELETE("auth/logout")
    Call<Void> logout(@Query("email") String email);
    @DELETE("products/product-by")
    Call<Void> deleteProduct(@Query("id") Integer id);
}
