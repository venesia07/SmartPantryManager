package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Intent;

public class RecipeList extends RecyclerView.Adapter<RecipeList.RecipeViewHolder> {

    private final ArrayList<Recipe> recipeList;

    public RecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipeList.get(position);

        holder.textViewRecipeName.setText(
                recipe.getName()
        );

        holder.textViewRecipeInstructions.setText(
                recipe.getInstructions()
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    RecipeDetailActivity.class
            );

            intent.putExtra(
                    "recipe_id",
                    recipe.getId()
            );

            intent.putExtra(
                    "recipe_name",
                    recipe.getName()
            );

            intent.putExtra(
                    "recipe_instructions",
                    recipe.getInstructions()
            );

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRecipeName;
        TextView textViewRecipeInstructions;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRecipeName =
                    itemView.findViewById(
                            R.id.textViewRecipeName
                    );

            textViewRecipeInstructions =
                    itemView.findViewById(
                            R.id.textViewRecipeInstructions
                    );
        }
    }
}