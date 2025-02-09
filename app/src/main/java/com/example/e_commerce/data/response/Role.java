package com.example.e_commerce.data.response;

import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("id")
    private Integer id;
    @SerializedName("roleName")
    private String roleName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
