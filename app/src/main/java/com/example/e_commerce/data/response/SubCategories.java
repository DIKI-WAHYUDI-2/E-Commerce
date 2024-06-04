package com.example.e_commerce.data.response;


public class SubCategories {
    private String name;
    private String image;
    private Categories category;

    public SubCategories(String name, String image, Categories category) {
        this.name = name;
        this.image = image;
        this.category = category;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
