package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply system bar spacing
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Pantry list
        recyclerViewPantry =
                findViewById(R.id.recyclerViewPantry);

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(MainActivity.this)
        );

        // Database
        databaseHelper =
                new DatabaseHelper(MainActivity.this);

        databaseHelper.seedRecipes();

        loadPantryItems();

        // Add Ingredient
        Button btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // Refresh Pantry
        Button btnRefreshPantry =
                findViewById(R.id.btnRefreshPantry);

        btnRefreshPantry.setOnClickListener(v -> {

            loadPantryItems();

            Toast.makeText(
                    MainActivity.this,
                    "Pantry refreshed",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Bottom Navigation
        Button navPantry =
                findViewById(R.id.navPantry);

        Button navRecipes =
                findViewById(R.id.navRecipes);

        Button navSettings =
                findViewById(R.id.navSettings);

        // Already on Pantry
        navPantry.setOnClickListener(v -> {
            // No navigation needed
        });

        navRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        navSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Automatically update pantry after adding or editing
        if (databaseHelper != null) {
            loadPantryItems();
        }
    }

    private void loadPantryItems() {

        ArrayList<Ingredient> ingredientList =
                databaseHelper.getAllIngredients();

        PantryItemList pantryItemList =
                new PantryItemList(ingredientList);

        recyclerViewPantry.setAdapter(pantryItemList);
    }
}