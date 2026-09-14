package com.example.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE pantry_items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "expiry_date TEXT" +
                        ")";

        db.execSQL(createPantryTable);


        String createRecipesTable =
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "instructions TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipesTable);


        String createRecipeIngredientsTable =
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                        ")";

        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        if (oldVersion < 2) {

            String createRecipesTable =
                    "CREATE TABLE IF NOT EXISTS recipes (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT NOT NULL, " +
                            "instructions TEXT NOT NULL" +
                            ")";

            db.execSQL(createRecipesTable);


            String createRecipeIngredientsTable =
                    "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "recipe_id INTEGER NOT NULL, " +
                            "ingredient_name TEXT NOT NULL, " +
                            "quantity REAL NOT NULL, " +
                            "unit TEXT NOT NULL, " +
                            "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                            ")";

            db.execSQL(createRecipeIngredientsTable);
        }
    }

    public long addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());

        long result = db.insert(
                "pantry_items",
                null,
                values
        );

        db.close();

        return result;
    }

    public ArrayList<Ingredient> getAllIngredients() {

        ArrayList<Ingredient> ingredientList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM pantry_items",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("quantity")
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit")
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow("expiry_date")
                );

                Ingredient ingredient = new Ingredient(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                ingredientList.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredientList;
    }
    public boolean updateIngredient(Ingredient ingredient) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());

        int rowsAffected = db.update(
                "pantry_items",
                values,
                "id = ?",
                new String[]{String.valueOf(ingredient.getId())}
        );

        return rowsAffected > 0;
    }

    public boolean deleteIngredient(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                "pantry_items",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsDeleted > 0;
    }

    public long addRecipe(String name, String instructions) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("instructions", instructions);

        return db.insert(
                "recipes",
                null,
                values
        );
    }

    public long addRecipeIngredient(
            int recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName);
        values.put("quantity", quantity);
        values.put("unit", unit);

        return db.insert(
                "recipe_ingredients",
                null,
                values
        );
    }

    public void seedRecipes() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes",
                null
        );

        cursor.moveToFirst();

        int recipeCount = cursor.getInt(0);

        cursor.close();

        if (recipeCount > 0) {
            return;
        }


        // 1. Scrambled Eggs
        long scrambledEggsId = addRecipe(
                "Scrambled Eggs",
                "Beat the eggs. Melt the butter in a pan, add the eggs and stir until cooked."
        );

        addRecipeIngredient(
                (int) scrambledEggsId,
                "eggs",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) scrambledEggsId,
                "butter",
                10,
                "g"
        );


        // 2. French Toast
        long frenchToastId = addRecipe(
                "French Toast",
                "Beat the eggs with milk. Dip the bread into the mixture and cook in a buttered pan until golden."
        );

        addRecipeIngredient(
                (int) frenchToastId,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                (int) frenchToastId,
                "eggs",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) frenchToastId,
                "milk",
                100,
                "ml"
        );

        addRecipeIngredient(
                (int) frenchToastId,
                "butter",
                10,
                "g"
        );


        // 3. Cheese Toast
        long cheeseToastId = addRecipe(
                "Cheese Toast",
                "Place cheese on the bread and toast until the bread is crisp and the cheese has melted."
        );

        addRecipeIngredient(
                (int) cheeseToastId,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                (int) cheeseToastId,
                "cheese",
                50,
                "g"
        );


        // 4. Egg Fried Rice
        long eggFriedRiceId = addRecipe(
                "Egg Fried Rice",
                "Cook the egg in a pan, add the cooked rice and oil, then stir together until heated through."
        );

        addRecipeIngredient(
                (int) eggFriedRiceId,
                "rice",
                200,
                "g"
        );

        addRecipeIngredient(
                (int) eggFriedRiceId,
                "eggs",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) eggFriedRiceId,
                "oil",
                15,
                "ml"
        );


        // 5. Pancakes
        long pancakesId = addRecipe(
                "Pancakes",
                "Mix flour, milk and egg into a smooth batter. Cook small portions in a pan until golden on both sides."
        );

        addRecipeIngredient(
                (int) pancakesId,
                "flour",
                150,
                "g"
        );

        addRecipeIngredient(
                (int) pancakesId,
                "milk",
                200,
                "ml"
        );

        addRecipeIngredient(
                (int) pancakesId,
                "eggs",
                1,
                "pieces"
        );


        // 6. Grilled Cheese Sandwich
        long grilledCheeseId = addRecipe(
                "Grilled Cheese Sandwich",
                "Place cheese between two slices of bread. Butter the outside and cook in a pan until golden."
        );

        addRecipeIngredient(
                (int) grilledCheeseId,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                (int) grilledCheeseId,
                "cheese",
                50,
                "g"
        );

        addRecipeIngredient(
                (int) grilledCheeseId,
                "butter",
                10,
                "g"
        );


        // 7. Tomato Sandwich
        long tomatoSandwichId = addRecipe(
                "Tomato Sandwich",
                "Slice the tomato and place it between two slices of bread."
        );

        addRecipeIngredient(
                (int) tomatoSandwichId,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                (int) tomatoSandwichId,
                "tomato",
                1,
                "pieces"
        );


        // 8. Cheese Omelette
        long cheeseOmeletteId = addRecipe(
                "Cheese Omelette",
                "Beat the eggs, cook in a pan, add cheese and fold the omelette before serving."
        );

        addRecipeIngredient(
                (int) cheeseOmeletteId,
                "eggs",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) cheeseOmeletteId,
                "cheese",
                50,
                "g"
        );


        // 9. Tomato Omelette
        long tomatoOmeletteId = addRecipe(
                "Tomato Omelette",
                "Beat the eggs, add chopped tomato and cook the mixture in a pan."
        );

        addRecipeIngredient(
                (int) tomatoOmeletteId,
                "eggs",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) tomatoOmeletteId,
                "tomato",
                1,
                "pieces"
        );


        // 10. Mashed Potatoes
        long mashedPotatoesId = addRecipe(
                "Mashed Potatoes",
                "Boil the potatoes until soft, then mash with milk and butter."
        );

        addRecipeIngredient(
                (int) mashedPotatoesId,
                "potato",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) mashedPotatoesId,
                "milk",
                100,
                "ml"
        );

        addRecipeIngredient(
                (int) mashedPotatoesId,
                "butter",
                20,
                "g"
        );


        // 11. Buttered Potatoes
        long butteredPotatoesId = addRecipe(
                "Buttered Potatoes",
                "Boil the potatoes until soft and mix them with butter."
        );

        addRecipeIngredient(
                (int) butteredPotatoesId,
                "potato",
                2,
                "pieces"
        );

        addRecipeIngredient(
                (int) butteredPotatoesId,
                "butter",
                20,
                "g"
        );


        // 12. Chicken Strips
        long chickenStripsId = addRecipe(
                "Chicken Strips",
                "Slice the chicken into strips, coat lightly with flour and cook in oil until fully cooked."
        );

        addRecipeIngredient(
                (int) chickenStripsId,
                "chicken",
                250,
                "g"
        );

        addRecipeIngredient(
                (int) chickenStripsId,
                "flour",
                50,
                "g"
        );

        addRecipeIngredient(
                (int) chickenStripsId,
                "oil",
                30,
                "ml"
        );


        // 13. Chicken and Rice
        long chickenRiceId = addRecipe(
                "Chicken and Rice",
                "Cook the chicken in a pan and serve it with cooked rice."
        );

        addRecipeIngredient(
                (int) chickenRiceId,
                "chicken",
                250,
                "g"
        );

        addRecipeIngredient(
                (int) chickenRiceId,
                "rice",
                200,
                "g"
        );


        // 14. Tomato Pasta
        long tomatoPastaId = addRecipe(
                "Tomato Pasta",
                "Cook the pasta, add chopped tomato and heat together before serving."
        );

        addRecipeIngredient(
                (int) tomatoPastaId,
                "pasta",
                200,
                "g"
        );

        addRecipeIngredient(
                (int) tomatoPastaId,
                "tomato",
                2,
                "pieces"
        );


        // 15. Chicken Pasta
        long chickenPastaId = addRecipe(
                "Chicken Pasta",
                "Cook the pasta and chicken separately, then combine them before serving."
        );

        addRecipeIngredient(
                (int) chickenPastaId,
                "pasta",
                200,
                "g"
        );

        addRecipeIngredient(
                (int) chickenPastaId,
                "chicken",
                250,
                "g"
        );


        // 16. Banana Oatmeal
        long bananaOatmealId = addRecipe(
                "Banana Oatmeal",
                "Cook the oats with milk and top with sliced banana."
        );

        addRecipeIngredient(
                (int) bananaOatmealId,
                "oats",
                100,
                "g"
        );

        addRecipeIngredient(
                (int) bananaOatmealId,
                "milk",
                250,
                "ml"
        );

        addRecipeIngredient(
                (int) bananaOatmealId,
                "banana",
                1,
                "pieces"
        );


        // 17. Chocolate Muffins
        long chocolateMuffinsId = addRecipe(
                "Chocolate Muffins",
                "Mix the flour, sugar, cocoa, egg, milk and butter. Place into muffin cups and bake until cooked."
        );

        addRecipeIngredient(
                (int) chocolateMuffinsId,
                "flour",
                200,
                "g"
        );

        addRecipeIngredient(
                (int) chocolateMuffinsId,
                "sugar",
                100,
                "g"
        );

        addRecipeIngredient(
                (int) chocolateMuffinsId,
                "cocoa",
                30,
                "g"
        );

        addRecipeIngredient(
                (int) chocolateMuffinsId,
                "eggs",
                1,
                "pieces"
        );

        addRecipeIngredient(
                (int) chocolateMuffinsId,
                "milk",
                150,
                "ml"
        );

        addRecipeIngredient(
                (int) chocolateMuffinsId,
                "butter",
                50,
                "g"
        );


        // 18. Vetkoek
        long vetkoekId = addRecipe(
                "Vetkoek",
                "Mix the flour with water to form a dough. Shape into small portions and fry in oil until golden."
        );

        addRecipeIngredient(
                (int) vetkoekId,
                "flour",
                250,
                "g"
        );

        addRecipeIngredient(
                (int) vetkoekId,
                "water",
                150,
                "ml"
        );

        addRecipeIngredient(
                (int) vetkoekId,
                "oil",
                100,
                "ml"
        );
    }

    private String normalizeIngredientName(String name) {

        if (name == null) {
            return "";
        }

        name = name.trim().toLowerCase();

        if (name.endsWith("ies")) {
            name = name.substring(0, name.length() - 3) + "y";
        }
        else if (name.endsWith("oes")) {
            name = name.substring(0, name.length() - 2);
        }
        else if (name.endsWith("s") && name.length() > 1) {
            name = name.substring(0, name.length() - 1);
        }

        return name;
    }

    private boolean pantryHasEnough(
            String requiredName,
            double requiredQuantity,
            String requiredUnit) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT name, quantity, unit FROM pantry_items",
                null
        );

        boolean hasEnough = false;

        while (cursor.moveToNext()) {

            String pantryName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            double pantryQuantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity")
                    );

            String pantryUnit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    );

            String normalPantryName =
                    normalizeIngredientName(pantryName);

            String normalRequiredName =
                    normalizeIngredientName(requiredName);

            if (normalPantryName.equals(normalRequiredName)
                    && pantryUnit.trim().equalsIgnoreCase(requiredUnit.trim())
                    && pantryQuantity >= requiredQuantity) {

                hasEnough = true;
                break;
            }
        }

        cursor.close();

        return hasEnough;
    }
    private boolean canMakeRecipe(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ingredient_name, quantity, unit " +
                        "FROM recipe_ingredients " +
                        "WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        boolean canMake = true;

        while (cursor.moveToNext()) {

            String ingredientName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("ingredient_name")
                    );

            double requiredQuantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity")
                    );

            String requiredUnit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    );

            if (!pantryHasEnough(
                    ingredientName,
                    requiredQuantity,
                    requiredUnit)) {

                canMake = false;
                break;
            }
        }

        cursor.close();

        return canMake;
    }

    public ArrayList<Recipe> getSuggestedRecipes() {

        ArrayList<Recipe> suggestedRecipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, instructions FROM recipes",
                null
        );

        while (cursor.moveToNext()) {

            int recipeId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    );

            String recipeName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String instructions =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("instructions")
                    );

            if (canMakeRecipe(recipeId)) {

                Recipe recipe = new Recipe(
                        recipeId,
                        recipeName,
                        instructions
                );

                suggestedRecipes.add(recipe);
            }
        }

        cursor.close();

        return suggestedRecipes;
    }

    public ArrayList<String> getRecipeIngredients(int recipeId) {

        ArrayList<String> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ingredient_name, quantity, unit " +
                        "FROM recipe_ingredients " +
                        "WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        while (cursor.moveToNext()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("ingredient_name")
                    );

            double quantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity")
                    );

            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    );

            ingredients.add(
                    quantity + " " + unit + " " + name
            );
        }

        cursor.close();

        return ingredients;
    }

}