package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditIngredientActivity extends AppCompatActivity {

    private int ingredientId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);

        EditText editTextIngredientName =
                findViewById(R.id.editTextIngredientName);

        EditText editTextQuantity =
                findViewById(R.id.editTextQuantity);

        EditText editTextUnit =
                findViewById(R.id.editTextUnit);

        EditText editTextExpiryDate =
                findViewById(R.id.editTextExpiryDate);

        Button btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);


        // CHECK IF WE ARE EDITING AN EXISTING INGREDIENT
        if (getIntent().hasExtra("ingredient_id")) {

            isEditMode = true;

            ingredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String ingredientName =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double ingredientQuantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            0
                    );

            String ingredientUnit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String ingredientExpiry =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );


            // PREFILL THE FORM
            editTextIngredientName.setText(ingredientName);

            editTextQuantity.setText(
                    String.valueOf(ingredientQuantity)
            );

            editTextUnit.setText(ingredientUnit);

            editTextExpiryDate.setText(ingredientExpiry);

            btnSaveIngredient.setText("Update Ingredient");
        }


        // EXPIRY DATE PICKER
        editTextExpiryDate.setOnClickListener(v -> {

            java.util.Calendar calendar =
                    java.util.Calendar.getInstance();

            int year =
                    calendar.get(java.util.Calendar.YEAR);

            int month =
                    calendar.get(java.util.Calendar.MONTH);

            int day =
                    calendar.get(java.util.Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            AddEditIngredientActivity.this,
                            (view,
                             selectedYear,
                             selectedMonth,
                             selectedDay) -> {

                                String selectedDate =
                                        String.format(
                                                java.util.Locale.getDefault(),
                                                "%02d/%02d/%04d",
                                                selectedDay,
                                                selectedMonth + 1,
                                                selectedYear
                                        );

                                editTextExpiryDate.setText(
                                        selectedDate
                                );
                            },
                            year,
                            month,
                            day
                    );

            datePickerDialog
                    .getDatePicker()
                    .setMinDate(
                            System.currentTimeMillis()
                    );

            datePickerDialog.show();
        });


        // SAVE / UPDATE BUTTON
        btnSaveIngredient.setOnClickListener(v -> {

            String name =
                    editTextIngredientName
                            .getText()
                            .toString()
                            .trim();

            String quantityText =
                    editTextQuantity
                            .getText()
                            .toString()
                            .trim();

            String unit =
                    editTextUnit
                            .getText()
                            .toString()
                            .trim();

            String expiryDate =
                    editTextExpiryDate
                            .getText()
                            .toString()
                            .trim();


            // VALIDATION
            if (name.isEmpty()) {

                editTextIngredientName.setError(
                        "Ingredient name is required"
                );

                return;
            }

            if (quantityText.isEmpty()) {

                editTextQuantity.setError(
                        "Quantity is required"
                );

                return;
            }

            double quantity;

            try {

                quantity =
                        Double.parseDouble(quantityText);

            } catch (NumberFormatException e) {

                editTextQuantity.setError(
                        "Enter a valid quantity"
                );

                return;
            }


            if (quantity <= 0) {

                editTextQuantity.setError(
                        "Quantity must be greater than 0"
                );

                return;
            }


            if (unit.isEmpty()) {

                editTextUnit.setError(
                        "Unit is required"
                );

                return;
            }


            if (!unit.matches("[a-zA-Z]+")) {

                editTextUnit.setError(
                        "Unit should contain letters only"
                );

                return;
            }


            DatabaseHelper databaseHelper =
                    new DatabaseHelper(
                            AddEditIngredientActivity.this
                    );


            // EDIT MODE
            if (isEditMode) {

                Ingredient ingredient =
                        new Ingredient(
                                ingredientId,
                                name,
                                quantity,
                                unit,
                                expiryDate
                        );

                boolean updated =
                        databaseHelper.updateIngredient(
                                ingredient
                        );

                if (updated) {

                    Toast.makeText(
                            AddEditIngredientActivity.this,
                            "Ingredient updated successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();

                } else {

                    Toast.makeText(
                            AddEditIngredientActivity.this,
                            "Failed to update ingredient",
                            Toast.LENGTH_SHORT
                    ).show();
                }


            } else {

                // ADD MODE
                Ingredient ingredient =
                        new Ingredient(
                                name,
                                quantity,
                                unit,
                                expiryDate
                        );

                long result =
                        databaseHelper.addIngredient(
                                ingredient
                        );

                if (result != -1) {

                    Toast.makeText(
                            AddEditIngredientActivity.this,
                            "Ingredient saved successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();

                } else {

                    Toast.makeText(
                            AddEditIngredientActivity.this,
                            "Failed to save ingredient",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat
                                            .Type
                                            .systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                });
    }
}