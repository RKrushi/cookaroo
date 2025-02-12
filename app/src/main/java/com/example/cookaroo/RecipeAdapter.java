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

import com.squareup.picasso.Picasso;

import java.util.List;

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

        // Open Recipe Details
        holder.btnShowRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("imageUrl", recipe.getImageUrl());
            context.startActivity(intent);
        });

        // Like Animation
        holder.btnLike.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
            holder.btnLike.startAnimation(animation);
            Toast.makeText(context, "Liked! ❤️", Toast.LENGTH_SHORT).show();
        });

        // Save Animation
        holder.btnSave.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
            holder.btnSave.startAnimation(animation);
            Toast.makeText(context, "Saved! 📌", Toast.LENGTH_SHORT).show();
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
}
