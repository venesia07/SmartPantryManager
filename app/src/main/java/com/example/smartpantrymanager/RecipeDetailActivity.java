package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textViewRecipeDetailName;
    private TextView textViewRecipeIngredients;
    private TextView textViewRecipeDetailInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        textViewRecipeDetailName =
                findViewById(R.id.textViewRecipeDetailName);

        textViewRecipeIngredients =
                findViewById(R.id.textViewRecipeIngredients);

        textViewRecipeDetailInstructions =
                findViewById(R.id.textViewRecipeDetailInstructions);

        int recipeId =
                getIntent().getIntExtra("recipe_id", -1);

        String recipeName =
                getIntent().getStringExtra("recipe_name");

        String instructions =
                getIntent().getStringExtra("recipe_instructions");

        textViewRecipeDetailName.setText(recipeName);

        textViewRecipeDetailInstructions.setText(instructions);

        DatabaseHelper databaseHelper =
                new DatabaseHelper(RecipeDetailActivity.this);

        ArrayList<String> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredientText =
                new StringBuilder();

        for (String ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient)
                    .append("\n");
        }

        textViewRecipeIngredients.setText(
                ingredientText.toString().trim()
        );
    }
}