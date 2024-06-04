package com.example.e_commerce.data.response;

import java.util.List;

public class SubCategoriesResponse {

    private List<SubCategories> data;
    private String message;
    private int status;

    public SubCategoriesResponse(List<SubCategories> data) {
        this.data = data;
    }

    public List<SubCategories> getData(){
        return data;
    }

    public void setData(List<SubCategories> data){
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
