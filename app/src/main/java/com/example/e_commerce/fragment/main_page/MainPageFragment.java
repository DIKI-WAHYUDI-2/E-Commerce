package com.example.e_commerce.fragment.main_page;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.AdapterSales;
import com.example.e_commerce.adapter.AdapterSales2;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.response.ProductResponse;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageFragment extends Fragment {
    private RecyclerView recyclerView,recyclerView2;
    private AdapterSales adapterSales;
    private AdapterSales2 adapterSales2;
    private MainPageViewModel viewModel;
    private List<Product> products,products2;
    private ProgressBar progressBar,progressBar2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        products = new ArrayList<>();
        products2 = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView2 = view.findViewById(R.id.recycleView2);
        progressBar2 = view.findViewById(R.id.ProgressBarMainPage2);
        progressBar = view.findViewById(R.id.ProgressBarMainPage);
        adapterSales = new AdapterSales(products);
        adapterSales2 = new AdapterSales2(products2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setAdapter(adapterSales);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setAdapter(adapterSales2);
        recyclerView2.setLayoutManager(layoutManager2);

        String email = "dikiwahyudi@gmail.com";
        String password = "123456";

        viewModel = new ViewModelProvider(this, new MainPageViewModelFactory(email,password)).get(MainPageViewModel.class);
        showLoading(true);
        viewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse products) {
                adapterSales.setDataList(products.getData());
                showLoading(false);
            }
        });

        getAllProductOrderByNameDesc();

        return view;
    }

    private void getAllProductOrderByNameDesc(){
        showLoading2(true);
        ApiService config = ApiConfigAuthenticated.getApiService("dikiwahyudi@gmail.com","123456");
        Call<ProductResponse> call = config.getAllProductOrderByNameDes();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                showLoading2(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    adapterSales2.setDataList(response.body().getData());
                    Log.d("MainPage Fragment", "Response Successfully retrivied data!");
                } else {
                    Log.d("MainPage Fragment", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                showLoading2(false);
                Log.d("MainPage Fragment","Failed fetch data " + t.getMessage());
            }
        });
    }

    public void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showLoading2(Boolean isLoading) {
        if (isLoading) {
            progressBar2.setVisibility(View.VISIBLE);
        } else {
            progressBar2.setVisibility(View.GONE);
        }
    }

    class MainPageViewModelFactory implements ViewModelProvider.Factory {
        private String email,password;

        public MainPageViewModelFactory(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MainPageViewModel.class)) {
                return (T) new MainPageViewModel(email, password);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}