package com.example.cookaroo;

public class RecipeModel {
    private int id;
    private String title;
    private String imageUrl;

    public RecipeModel(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
