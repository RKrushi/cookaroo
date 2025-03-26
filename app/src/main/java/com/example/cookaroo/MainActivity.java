package com.example.cookaroo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private static final String API_KEY = "fc807d2d1fb34b89bb828b8293c8b29f";
    private static final String API_KEY = "";
    private EditText etIngredients;
    private Button btnFindRecipes;
    private RecyclerView rvRecipes;
    private RecipeAdapter recipeAdapter;
    private List<RecipeModel> recipeList;
    private RequestQueue requestQueue;
    private Animation fadeIn, slideUp;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar and Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_saved_recipes) {
            startActivity(new Intent(this, SavedRecipesActivity.class));
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(this, ContactUsActivity.class));
            Toast.makeText(this, "Countact clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutUsActivity.class));
        } else if (id == R.id.nav_logout) {
            logoutUser();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        sessionManager.logout(); // Clear session
        FirebaseAuth.getInstance().signOut(); // Sign out from Firebase Auth
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
