package com.example.e_commerce.data.response;

import com.google.gson.annotations.SerializedName;

public class CartItem {

    @SerializedName("quantity")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartItem(Integer quantity) {
        this.quantity = quantity;
    }
}
