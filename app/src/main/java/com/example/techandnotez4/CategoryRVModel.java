package com.example.techandnotez4;

public class CategoryRVModel {

    private String category;
    private String CategoryImageUrl;

    public CategoryRVModel(String category, String categoryImageUrl) {
        this.category = category;
        CategoryImageUrl = categoryImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryImageUrl() {
        return CategoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        CategoryImageUrl = categoryImageUrl;
    }
}
