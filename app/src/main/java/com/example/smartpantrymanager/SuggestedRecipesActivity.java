package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.Button;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView textViewNoRecipes;
    private DatabaseHelper databaseHelper;
    private View layoutNoRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_suggested_recipes
        );

        recyclerViewRecipes =
                findViewById(
                        R.id.recyclerViewRecipes
                );

        textViewNoRecipes =
                findViewById(
                        R.id.textViewNoRecipes
                );

        layoutNoRecipes =
                findViewById(R.id.layoutNoRecipes);

        databaseHelper =
                new DatabaseHelper(
                        SuggestedRecipesActivity.this
                );

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(
                        SuggestedRecipesActivity.this
                )
        );

        loadSuggestedRecipes();

        Button navPantry = findViewById(R.id.navPantry);
        Button navSettings = findViewById(R.id.navSettings);

        navPantry.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    private void loadSuggestedRecipes() {

        ArrayList<Recipe> recipeList =
                databaseHelper.getSuggestedRecipes();

        if (recipeList.isEmpty()) {

            recyclerViewRecipes.setVisibility(View.GONE);
            layoutNoRecipes.setVisibility(View.VISIBLE);

        } else {

            recyclerViewRecipes.setVisibility(View.VISIBLE);
            layoutNoRecipes.setVisibility(View.GONE);

            RecipeList recipeListDisplay =
                    new RecipeList(recipeList);

            recyclerViewRecipes.setAdapter(recipeListDisplay);
        }
    }
}