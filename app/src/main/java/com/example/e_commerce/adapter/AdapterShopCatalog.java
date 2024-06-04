package com.example.e_commerce.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.DetailCatalog;
import com.example.e_commerce.R;
import com.example.e_commerce.ShopCatalog;
import com.example.e_commerce.data.response.Image;
import com.example.e_commerce.data.response.Product;

import java.util.List;

public class AdapterShopCatalog extends RecyclerView.Adapter<AdapterShopCatalog.ViewHolder>{

    private List<Product> products;

    public AdapterShopCatalog(List<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public AdapterShopCatalog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_catalog,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShopCatalog.ViewHolder holder, int position) {

        Product product = products.get(position);
        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText("Rp " + product.getPrice().toString());
        holder.brandProduct.setText(product.getBrand());

        List<Image> images = product.getImage();
        if (images != null && !images.isEmpty()) {
            Bitmap bitmap = decodeBase64ToBitmap(images.get(0).getImageUrl());
            holder.imgProduct.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(click-> {
            Intent intent = new Intent(click.getContext(), DetailCatalog.class);
            intent.putExtra("NAME_PRODUCT", product.getName());
            click.getContext().startActivity(intent);
        });
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void setProductList(List<Product> productList) {
        this.products = productList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameProduct,priceProduct,brandProduct;
        private ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.nameListCatalog);
            priceProduct = itemView.findViewById(R.id.priceListCatalog);
            brandProduct = itemView.findViewById(R.id.brandListCatalog);
            imgProduct = itemView.findViewById(R.id.imgListCatalog);
        }
    }
}
