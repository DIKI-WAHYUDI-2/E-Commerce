package com.example.e_commerce.data.response;

import java.util.List;

public class CategoriesResponse {

    private List<Categories> data;
    private String message;
    private int status;

    public CategoriesResponse(List<Categories> data) {
        this.data = data;
    }

    public List<Categories> getData(){
        return data;
    }

    public void setData(List<Categories> data){
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
