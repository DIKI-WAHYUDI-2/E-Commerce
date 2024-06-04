package com.example.e_commerce.data.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private Integer price;
    @SerializedName("image")
    private List<Image> image;
    @SerializedName("brand")
    private String brand;
    @SerializedName("description")
    private String description;
    @SerializedName("cartItem")
    private CartItem cartItem;

    public Product(Integer id, String name, Integer price, List<Image> image, String brand, String description, CartItem cartItem) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.brand = brand;
        this.description = description;
        this.cartItem = cartItem;
    }

    public Product(){

    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }
}
