    package com.example.e_commerce.fragment.bag;

    import androidx.lifecycle.Observer;
    import androidx.lifecycle.ViewModel;
    import androidx.lifecycle.ViewModelProvider;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;

    import com.example.e_commerce.R;
    import com.example.e_commerce.SuccesShopping;
    import com.example.e_commerce.adapter.AdapterBag;
    import com.example.e_commerce.data.response.Product;
    import com.example.e_commerce.data.response.ProductResponse;
    import com.example.e_commerce.databinding.FragmentBagBinding;
    import com.example.e_commerce.fragment.main_page.MainPageViewModel;

    import java.util.ArrayList;
    import java.util.List;

    public class BagFragment extends Fragment implements AdapterBag.OnTotalAmountChangeListener {

        private RecyclerView recyclerView;
        private AdapterBag adapterBag;
        private BagViewModel viewModel;
        private List<Product> products;
        private TextView totalAmount;
        private Button button;


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            products = new ArrayList<>();
            View view = inflater.inflate(R.layout.fragment_bag, container, false);
            recyclerView = view.findViewById(R.id.recycleViewBag);
            totalAmount = view.findViewById(R.id.totalAmount);
            button = view.findViewById(R.id.btnCheckout);
            adapterBag = new AdapterBag(products);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

            recyclerView.setAdapter(adapterBag);
            recyclerView.setLayoutManager(layoutManager);

            String email = "dikiwahyudi@gmail.com";
            String password = "123456";

            adapterBag.setOnTotalAmountChangeListener(this);

            viewModel = new ViewModelProvider(this, new BagViewModelFactory(email,password)).get(BagViewModel.class);
            viewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
                @Override
                public void onChanged(ProductResponse productResponse) {
                    adapterBag.setDataList(productResponse.getData());
                }
            });

            button.setOnClickListener(c-> {
                Intent intent = new Intent(getActivity(), SuccesShopping.class);
                startActivity(intent);
            });

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            viewModel.refreshData(); // Refresh data when fragment resumes
        }

        @Override
        public void onTotalAmountChanged(int totalAmount) {
            this.totalAmount.setText("Rp" + totalAmount);
        }

        public void updateProductList(List<Product> newProductList) {
            adapterBag.setDataList(newProductList);
        }

        class BagViewModelFactory implements ViewModelProvider.Factory {
            private String email,password;

            public BagViewModelFactory(String email, String password){
                this.email = email;
                this.password = password;
            }

            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                if (modelClass.isAssignableFrom(BagViewModel.class)) {
                    return (T) new BagViewModel(email, password);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }


    }