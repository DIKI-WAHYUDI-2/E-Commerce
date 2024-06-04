package com.example.e_commerce.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.DetailCatalog;
import com.example.e_commerce.R;
import com.example.e_commerce.data.response.Image;
import com.example.e_commerce.data.response.Product;

import java.util.List;

public class AdapterSales extends RecyclerView.Adapter<AdapterSales.ViewHolder> {

    private List<Product> products;
    public AdapterSales(List<Product> Products){
        this.products = Products;
    }
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    @NonNull
    @Override
    public AdapterSales.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_sales,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSales.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameProduct.setText(product.getName());
        holder.price.setText("Rp " + product.getPrice().toString());

        List<Image> images = product.getImage();
        if (images != null && !images.isEmpty()) {
            Bitmap bitmap = decodeBase64ToBitmap(images.get(0).getImageUrl());
            holder.imageView.setImageBitmap(bitmap);
        }

        holder.loveImg.setSelected(sparseBooleanArray.get(position,false));

        holder.loveImg.setOnClickListener(click->{
            boolean isSelected = !click.isSelected();
            click.setSelected(isSelected);
            sparseBooleanArray.put(position,isSelected);
        });

        holder.itemView.setOnClickListener(click -> {
            Intent intent = new Intent(click.getContext(), DetailCatalog.class);
            intent.putExtra("NAME_PRODUCT", product.getName());
            click.getContext().startActivity(intent);
        });
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void setDataList(List<Product> newDataList){
        this.products = newDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView, loveImg;
        private TextView nameProduct,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSales);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            price = itemView.findViewById(R.id.price);
            loveImg = itemView.findViewById(R.id.loveImg);
        }
    }
}
