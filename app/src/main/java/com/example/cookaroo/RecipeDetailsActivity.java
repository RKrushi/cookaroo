package com.example.cookaroo;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = "fc807d2d1fb34b89bb828b8293c8b29f";
    private ImageView ivRecipeImage;
    private TextView tvIngredients, tvSteps;
    private Button btnClose;
    private RequestQueue requestQueue;
    private static final String TAG = "RecipeDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvSteps = findViewById(R.id.tvSteps);
        btnClose = findViewById(R.id.btnClose);

        requestQueue = Volley.newRequestQueue(this);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load Image with Animation
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(ivRecipeImage);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            ivRecipeImage.startAnimation(fadeIn);
        }

        if (recipeId != -1) {
            fetchRecipeDetails(recipeId);
        } else {
            Toast.makeText(this, "Recipe ID not found!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Recipe ID is invalid!");
        }

        // Add bounce animation to close button
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        btnClose.startAnimation(bounce);

        btnClose.setOnClickListener(v -> finish());
    }

    private void fetchRecipeDetails(int recipeId) {
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Extract ingredients
                        if (response.has("extendedIngredients")) {
                            JSONArray ingredientsArray = response.getJSONArray("extendedIngredients");
                            StringBuilder ingredientsList = new StringBuilder("Ingredients:\n");
                            for (int i = 0; i < ingredientsArray.length(); i++) {
                                JSONObject ingredient = ingredientsArray.getJSONObject(i);
                                ingredientsList.append("- ")
                                        .append(ingredient.getString("original"))
                                        .append("\n");
                            }
                            tvIngredients.setText(ingredientsList.toString());
                        } else {
                            tvIngredients.setText("No ingredients found.");
                            Log.e(TAG, "No extendedIngredients field in response.");
                        }

                        // Extract steps
                        if (response.has("analyzedInstructions") && response.getJSONArray("analyzedInstructions").length() > 0) {
                            JSONArray instructionsArray = response.getJSONArray("analyzedInstructions");
                            StringBuilder stepsList = new StringBuilder("Steps:\n");

                            JSONArray stepsArray = instructionsArray.getJSONObject(0).getJSONArray("steps");
                            for (int i = 0; i < stepsArray.length(); i++) {
                                JSONObject step = stepsArray.getJSONObject(i);
                                stepsList.append(i + 1)
                                        .append(". ")
                                        .append(step.getString("step"))
                                        .append("\n");
                            }
                            tvSteps.setText(stepsList.toString());
                        } else {
                            tvSteps.setText("No cooking instructions available.");
                            Log.e(TAG, "No analyzedInstructions field in response.");
                        }

                        // Fade-in animation for text
                        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                        tvIngredients.startAnimation(fadeIn);
                        tvSteps.startAnimation(fadeIn);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing Error: " + e.getMessage());
                        Toast.makeText(this, "Error parsing recipe details!", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Log.e(TAG, "API Request Error: " + error.getMessage());
            Toast.makeText(this, "Error fetching recipe details!", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(request);
    }
}
