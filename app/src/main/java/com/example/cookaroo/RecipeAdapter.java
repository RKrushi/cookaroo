package com.example.cookaroo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<RecipeModel> recipeList;

    public RecipeAdapter(Context context, List<RecipeModel> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeModel recipe = recipeList.get(position);
        holder.tvTitle.setText(recipe.getTitle());
        Picasso.get().load(recipe.getImageUrl()).into(holder.ivRecipeImage);

        // Show Recipe Button
        holder.btnShowRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("imageUrl", recipe.getImageUrl());
            context.startActivity(intent);
        });

        // Like Button Animation
        holder.btnLike.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
            holder.btnLike.startAnimation(animation);
            Toast.makeText(context, "Liked! ❤️", Toast.LENGTH_SHORT).show();
        });

        // Save Button Click (Save to Firebase)
        holder.btnSave.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
            holder.btnSave.startAnimation(animation);
            saveRecipeToFirebase(recipe);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRecipeImage;
        TextView tvTitle;
        Button btnShowRecipe;
        ImageButton btnLike, btnSave;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnShowRecipe = itemView.findViewById(R.id.btnShowRecipe);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnSave = itemView.findViewById(R.id.btnSave);
        }
    }

    private void saveRecipeToFirebase(RecipeModel recipe) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(context, "Please login to save recipes!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid()).child("SavedRecipes");

        String recipeId = String.valueOf(recipe.getId());
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("title", recipe.getTitle());
        recipeData.put("imageUrl", recipe.getImageUrl());
        recipeData.put("recipeId", recipeId);

        databaseReference.child(recipeId).setValue(recipeData)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Recipe Saved! 📌", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to save recipe!", Toast.LENGTH_SHORT).show());
    }
}
