package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPantry;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);

        databaseHelper =
                new DatabaseHelper(MainActivity.this);

        databaseHelper.seedRecipes();

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(MainActivity.this)
        );

        loadPantryItems();

        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

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

        Button btnSuggestedRecipes =
                findViewById(R.id.btnSuggestedRecipes);

        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        Button navRecipes =
                findViewById(R.id.navRecipes);

        Button navSettings =
                findViewById(R.id.navSettings);

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

    private void loadPantryItems() {

        ArrayList<Ingredient> ingredientList =
                databaseHelper.getAllIngredients();

        PantryItemList pantryItemList =
                new PantryItemList(ingredientList);

        recyclerViewPantry.setAdapter(pantryItemList);
    }


}