package com.example.cookaroo;

public class RecipeModel1 {
    private String recipeId;
    private String title;
    private String imageUrl;

    // Default constructor required for Firebase
    public RecipeModel1() { }

    // Parameterized constructor
    public RecipeModel1(String recipeId, String title, String imageUrl) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getRecipeId() { return recipeId; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
}
