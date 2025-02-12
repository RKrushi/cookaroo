package com.example.cookaroo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "fc807d2d1fb34b89bb828b8293c8b29f";
    private EditText etIngredients;
    private Button btnFindRecipes;
    private RecyclerView rvRecipes;
    private RecipeAdapter recipeAdapter;
    private List<RecipeModel> recipeList;
    private RequestQueue requestQueue;
    private Animation fadeIn, slideUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        etIngredients = findViewById(R.id.etIngredients);
        btnFindRecipes = findViewById(R.id.btnSearch);
        rvRecipes = findViewById(R.id.rvRecipes);

        // Load animations
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply animations
        etIngredients.startAnimation(fadeIn);
        btnFindRecipes.startAnimation(slideUp);
        rvRecipes.startAnimation(slideUp);

        // RecyclerView setup
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        rvRecipes.setAdapter(recipeAdapter);

        requestQueue = Volley.newRequestQueue(this);

        btnFindRecipes.setOnClickListener(v -> {
            String ingredients = etIngredients.getText().toString().trim();
            if (!ingredients.isEmpty()) {
                fetchRecipes(ingredients);
            } else {
                Toast.makeText(MainActivity.this, "Enter ingredients!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecipes(String ingredients) {
        String url = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + API_KEY + "&ingredients=" + ingredients + "&number=10";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        recipeList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject recipe = response.getJSONObject(i);
                            int recipeId = recipe.getInt("id");
                            String title = recipe.getString("title");
                            String imageUrl = recipe.getString("image");

                            recipeList.add(new RecipeModel(recipeId, title, imageUrl));
                        }
                        recipeAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("RecipeFetchError", "Error parsing recipes: " + e.getMessage());
                    }
                }, error -> Log.e("RecipeFetchError", "Error fetching recipes: " + error.getMessage()));

        requestQueue.add(request);
    }
}
