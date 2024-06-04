package com.example.e_commerce.fragment.shop;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_commerce.data.response.Categories;
import com.example.e_commerce.data.response.CategoriesResponse;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.response.SubCategoriesResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopViewModel extends ViewModel {

    private MutableLiveData<SubCategoriesResponse> dataList;
    private String email,password;

    public ShopViewModel(String email,String password){
        this.email = email;
        this.password = password;
        dataList = new MutableLiveData<>();
    }

    public LiveData<SubCategoriesResponse> getDataList(){
        return dataList;
    }

    public void loadData(String category){
        ApiService service = ApiConfigAuthenticated.getApiService(email,password);
        Call<SubCategoriesResponse> call = service.getSubCategorY(category);
        call.enqueue(new Callback<SubCategoriesResponse>() {
            @Override
            public void onResponse(Call<SubCategoriesResponse> call, Response<SubCategoriesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null){
                    dataList.setValue(response.body());
                }else {
                    Log.d("Shop ViewModel","Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<SubCategoriesResponse> call, Throwable t) {
                Log.d("Shop ViewModel","Failed fetch data " + t.getMessage());
            }
        });
    }
}