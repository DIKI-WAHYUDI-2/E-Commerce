package com.example.e_commerce.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.data.response.Image;
import com.example.e_commerce.data.response.Product;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBag extends RecyclerView.Adapter<AdapterBag.ViewHolder> {
    private List<Product> products;
    private String email, password;
    private OnTotalAmountChangeListener totalAmountChangeListener;

    public AdapterBag(List<Product> products){
        this.products = products;
    }
    @NonNull
    @Override
    public AdapterBag.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bag_catalog,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBag.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("Rp" + product.getPrice().toString());
        holder.quantity.setText(product.getCartItem().getQuantity().toString());

        List<Image> images = product.getImage();
        if (images != null && !images.isEmpty()) {
            Bitmap bitmap = decodeBase64ToBitmap(images.get(0).getImageUrl());
            holder.img.setImageBitmap(bitmap);
        }

        holder.imgMore.setOnClickListener(click->{
            removeFromCart(product.getId(), position, holder.itemView);

        });

        email = "dikiwahyudi@gmail.com";
        password = "123456";

    }
    public void removeItem(int position) {
        products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, products.size());
        calculateTotalAmount();
    }
    private void removeFromCart(Integer id, int position, View view){
        ApiService config = ApiConfigAuthenticated.getApiService(email,password);
        Call<Void> call = config.removeProductFromCart(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    removeItem(position);
                    Snackbar.make(view, "Success removed product from cart", Snackbar.LENGTH_LONG).show();
                    Log.d("AdapterBag", "Product removed successfully from cart");
                } else {
                    Log.e("AdapterBag", "Failed to remove product from cart. Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AdapterBag", "Failed to remove product from cart. Error: " + t.getMessage());
            }
        });

    }

    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void setDataList(List<Product> newDataList){
        this.products = newDataList;
        notifyDataSetChanged();
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        int totalAmount = 0;
        for (Product product : products) {
            totalAmount += product.getPrice() * product.getCartItem().getQuantity();
        }
        if (totalAmountChangeListener != null) {
            totalAmountChangeListener.onTotalAmountChanged(totalAmount);
        }
    }

    public void setOnTotalAmountChangeListener(OnTotalAmountChangeListener listener) {
        this.totalAmountChangeListener = listener;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnTotalAmountChangeListener {
        void onTotalAmountChanged(int totalAmount);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,quantity;
        ImageView img,imgMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameBagListCatalog);
            price = itemView.findViewById(R.id.priceBagListCatalog);
            quantity = itemView.findViewById(R.id.quantityBagListCatalog);
            img = itemView.findViewById(R.id.imgBagListCatalog);
            imgMore = itemView.findViewById(R.id.imgMore);
        }
    }
}
