package com.example.cookaroo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter1 extends RecyclerView.Adapter<RecipeAdapter1.ViewHolder> {
    private Context context;
    private List<Recipe> recipeList;

    public RecipeAdapter1(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.titleTextView.setText(recipe.getTitle());
        Picasso.get().load(recipe.getImageUrl()).into(holder.imageView);

        // Show full recipe details
        holder.showRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("imageUrl", recipe.getImageUrl());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("steps", recipe.getSteps());
            context.startActivity(intent);
        });

        // Remove Recipe
        holder.removeButton.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SavedRecipes").child(recipe.getId());
            ref.removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(context, "Recipe removed!", Toast.LENGTH_SHORT).show();
                recipeList.remove(position);
                notifyItemRemoved(position);
            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to remove recipe", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        Button showRecipeButton, removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recipeTitle1);
            imageView = itemView.findViewById(R.id.recipeImage1);
            showRecipeButton = itemView.findViewById(R.id.btnShowRecipe);
            removeButton = itemView.findViewById(R.id.btnRemoveRecipe);
        }
    }
}
