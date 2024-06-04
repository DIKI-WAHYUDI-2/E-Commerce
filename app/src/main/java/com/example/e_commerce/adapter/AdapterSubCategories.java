package com.example.e_commerce.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.ShopCatalog;
import com.example.e_commerce.data.response.Categories;
import com.example.e_commerce.data.response.SubCategories;

import java.util.List;

public class AdapterSubCategories extends RecyclerView.Adapter<AdapterSubCategories.ViewHolder> {

    private List<SubCategories> subCategoryList;

    public AdapterSubCategories(List<SubCategories> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @NonNull
    @Override
    public AdapterSubCategories.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shop_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubCategories.ViewHolder holder, int position) {
        SubCategories subCategory = subCategoryList.get(position);
        holder.name.setText(subCategory.getName());
        Bitmap bitmap = decodeBase64ToBitmap(subCategory.getImage());
        holder.image.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(click->{
            Intent intent = new Intent(click.getContext(), ShopCatalog.class);
            intent.putExtra("NAME_SUB_CATEGORY", subCategory.getName());
            intent.putExtra("NAME_CATEGORY", subCategory.getCategory().getName());
            click.getContext().startActivity(intent);
        });
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public void setSubCategoryList(List<SubCategories> subCategoryList) {
        this.subCategoryList = subCategoryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoriesName);
            image = itemView.findViewById(R.id.imgCategories);
        }
    }
}
