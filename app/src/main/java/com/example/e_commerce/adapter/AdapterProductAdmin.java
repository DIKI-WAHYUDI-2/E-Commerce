package com.example.e_commerce.adapter;

import static com.example.e_commerce.AdminProduct.UPDATE_PRODUCT_REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.AdminProduct;
import com.example.e_commerce.R;
import com.example.e_commerce.UpdateProduct;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterProductAdmin extends RecyclerView.Adapter<AdapterProductAdmin.ViewHolder> {

    private List<Product> products;
    public AdapterProductAdmin(List<Product> Products){
        this.products = Products;
    }

    @NonNull
    @Override
    public AdapterProductAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_of_products,parent,false);
        return new AdapterProductAdmin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductAdmin.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.brand.setText(product.getBrand());

        holder.edit.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), UpdateProduct.class);
            intent.putExtra("PRODUCT_NAME",product.getName());
            ((Activity) v.getContext()).startActivityForResult(intent, UPDATE_PRODUCT_REQUEST_CODE);
        });


        holder.remove.setOnClickListener(v->{
            Integer id = product.getId();
            deleteProduct(id, holder.itemView,position);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void deleteProduct(Integer id,View view,int position){
        ApiService config = ApiConfigAuthenticated.getApiService("dikiwahyudi@gmail.com","123456");
        Call<Void> call = config.deleteProduct(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    products.remove(position);
                    notifyItemRemoved(position);
                    Snackbar.make(view, "Success removed product from list", Snackbar.LENGTH_LONG).show();
                    Log.d("AdapterProductAdmin", "Product removed successfully from list");
                } else {
                    Log.e("AdapterProductAdmin", "Failed to remove product from list. Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AdapterProductAdmin", "Failed to remove product. Error: " + t.getMessage());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,brand;
        private ImageView edit,remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.listOfProuductName);
            brand = itemView.findViewById(R.id.listOfProuductBrand);
            edit = itemView.findViewById(R.id.editButtonLOP);
            remove = itemView.findViewById(R.id.removeButtonLOP);
        }
    }
}
