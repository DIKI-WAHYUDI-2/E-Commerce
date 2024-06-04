package com.example.e_commerce.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.ShopCatalog;
import com.example.e_commerce.data.response.Categories;

import java.util.List;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolder> {

    private List<Categories> categories;
    public AdapterCategories(List<Categories> categories){
        this.categories = categories;
    }

    @NonNull
    @Override
    public AdapterCategories.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shop_categories,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategories.ViewHolder holder, int position) {
        Categories categorie = categories.get(position);

        holder.nameCategories.setText(categorie.getName());

        holder.itemView.setOnClickListener(click->{
            Intent intent = new Intent(click.getContext(), ShopCatalog.class);
            intent.putExtra("NAME_CATEGORY", categorie.getName());
            click.getContext().startActivity(intent);
        });
    }

    public void setDataList(List<Categories> newDataList){
        this.categories = newDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameCategories;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategories = itemView.findViewById(R.id.categoriesName);
        }
    }
}
