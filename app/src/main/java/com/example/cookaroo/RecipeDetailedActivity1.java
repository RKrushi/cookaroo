package com.example.cookaroo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeDetailedActivity1 extends AppCompatActivity {
    private ImageView recipeImage;
    private TextView recipeTitle, recipeIngredients, recipeSteps;
    private String recipeId;
    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i="; // Example API

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detailed1);

        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeSteps = findViewById(R.id.recipeSteps);

        // Get Recipe ID from Intent
        recipeId = getIntent().getStringExtra("recipe_id");

        if (recipeId != null) {
            new FetchRecipeDetails().execute(API_URL + recipeId);
        } else {
            Toast.makeText(this, "Recipe not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchRecipeDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject recipeData = jsonObject.getJSONArray("meals").getJSONObject(0);

                String title = recipeData.getString("strMeal");
                String imageUrl = recipeData.getString("strMealThumb");
                String ingredients = "Ingredients: " + recipeData.getString("strIngredient1") + ", " + recipeData.getString("strIngredient2");
                String instructions = recipeData.getString("strInstructions");

                recipeTitle.setText(title);
                recipeIngredients.setText(ingredients);
                recipeSteps.setText(instructions);

                Glide.with(RecipeDetailedActivity1.this)
                        .load(imageUrl)
                        .into(recipeImage);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RecipeDetailedActivity1.this, "Error fetching details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
