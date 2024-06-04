package com.example.e_commerce.fragment.shop;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e_commerce.R;
import com.example.e_commerce.adapter.AdapterCategories;
import com.example.e_commerce.adapter.AdapterSubCategories;
import com.example.e_commerce.data.response.Categories;
import com.example.e_commerce.data.response.CategoriesResponse;
import com.example.e_commerce.data.response.SubCategories;
import com.example.e_commerce.data.response.SubCategoriesResponse;
import com.example.e_commerce.data.retrofit.ApiConfig;
import com.example.e_commerce.data.retrofit.ApiService;
import com.example.e_commerce.databinding.FragmentShopBinding;
import com.example.e_commerce.fragment.main_page.MainPageViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterCategories adapterCategories;
    private List<Categories> categories;
    private ShopViewModel viewModel;
    private AdapterSubCategories adapterSubCategories;
    private List<SubCategories> subCategories;
    private ImageView recWomen,recMen,recKids;
    private LinearLayout llWomen,llMen,llKids;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        subCategories = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerView = view.findViewById(R.id.recycleViewShop);
        adapterSubCategories = new AdapterSubCategories(subCategories);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(adapterSubCategories);
        recyclerView.setLayoutManager(layoutManager);

        String email = "dikiwahyudi@gmail.com";
        String password = "123456";

        viewModel = new ViewModelProvider(this, new ShopModelFactory(email, password)).get(ShopViewModel.class);
        viewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<SubCategoriesResponse>() {
            @Override
            public void onChanged(SubCategoriesResponse subCategoriesResponse) {
                if (subCategoriesResponse != null) {
                    adapterSubCategories.setSubCategoryList(subCategoriesResponse.getData());
                }
            }
        });

        recWomen = view.findViewById(R.id.recWomen);
        recMen = view.findViewById(R.id.recMen);
        recKids = view.findViewById(R.id.recKids);
        llKids = view.findViewById(R.id.llKids);
        llWomen = view.findViewById(R.id.llWomen);
        llMen = view.findViewById(R.id.llMen);

        llMen.setOnClickListener(click->{
            recMen.setVisibility(View.VISIBLE);
            recWomen.setVisibility(View.GONE);
            recKids.setVisibility(View.GONE);
            viewModel.loadData("Men");
        });

        llKids.setOnClickListener(click->{
            recMen.setVisibility(View.GONE);
            recWomen.setVisibility(View.GONE);
            recKids.setVisibility(View.VISIBLE);
            viewModel.loadData("Kids");
        });

        llWomen.setOnClickListener(click-> {
            recWomen.setVisibility(View.VISIBLE);
            recMen.setVisibility(View.GONE);
            recKids.setVisibility(View.GONE);
            viewModel.loadData("Women");
        });

        viewModel.loadData("Women");

        return view;
    }

    class ShopModelFactory implements ViewModelProvider.Factory {
        private String email,password;

        public ShopModelFactory(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ShopViewModel.class)) {
                return (T) new ShopViewModel(email, password);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }



}