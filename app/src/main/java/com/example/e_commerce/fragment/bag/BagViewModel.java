package com.example.e_commerce.fragment.bag;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BagViewModel extends ViewModel {

    private MutableLiveData<ProductResponse> dataList;
    private String email,password;

    public BagViewModel(String email,String password){
        this.email = email;
        this.password = password;
        loadData();
    }

    public LiveData<ProductResponse> getDataList(){
        if (dataList == null){
            dataList = new MutableLiveData<>();
        }
        return dataList;
    }

    private void loadData(){
        ApiService config = ApiConfigAuthenticated.getApiService(email,password);
        Call<ProductResponse> call = config.getAllDataByCart();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null){
                    dataList.setValue(response.body());
                }else {
                    Log.d("Bag ViewModel", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("Bag ViewModel","Failed fetch data " + t.getMessage());
            }
        });
    }

    public void refreshData() {
        loadData();
    }
}