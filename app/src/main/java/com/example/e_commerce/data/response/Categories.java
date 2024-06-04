package com.example.e_commerce.data.response;

import com.google.gson.annotations.SerializedName;

public class Categories {

    @SerializedName("name")
    private String name;

    public Categories(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
