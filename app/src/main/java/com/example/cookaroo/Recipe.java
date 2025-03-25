package com.example.cookaroo;

public class Recipe {
    private String id;
    private String title;
    private String imageUrl;
    private String ingredients;
    private String steps;

    // Empty constructor for Firebase
    public Recipe() {
    }

    public Recipe(String id, String title, String imageUrl, String ingredients, String steps) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }
}
