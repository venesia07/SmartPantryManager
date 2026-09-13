package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryItemList
        extends RecyclerView.Adapter<PantryItemList.PantryItemViewHolder> {

    private ArrayList<Ingredient> ingredientList;

    public PantryItemList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public PantryItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new PantryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryItemViewHolder holder,
            int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.textViewName.setText(ingredient.getName());

        holder.textViewQuantity.setText(
                ingredient.getQuantity() + " " + ingredient.getUnit()
        );

        if (ingredient.getExpiryDate() == null
                || ingredient.getExpiryDate().isEmpty()) {

            holder.textViewExpiry.setText("No expiry date");

        } else {

            holder.textViewExpiry.setText(
                    "Expires: " + ingredient.getExpiryDate()
            );
        }

        // EDIT BUTTON
        holder.btnEditIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    AddEditIngredientActivity.class
            );

            intent.putExtra("ingredient_id", ingredient.getId());
            intent.putExtra("ingredient_name", ingredient.getName());
            intent.putExtra(
                    "ingredient_quantity",
                    ingredient.getQuantity()
            );
            intent.putExtra("ingredient_unit", ingredient.getUnit());
            intent.putExtra(
                    "ingredient_expiry",
                    ingredient.getExpiryDate()
            );

            holder.itemView.getContext().startActivity(intent);
        });

        // DELETE BUTTON
        holder.btnDeleteIngredient.setOnClickListener(v -> {

            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Ingredient")
                    .setMessage(
                            "Are you sure you want to delete "
                                    + ingredient.getName() + "?"
                    )
                    .setPositiveButton("Delete", (dialog, which) -> {

                        DatabaseHelper databaseHelper =
                                new DatabaseHelper(
                                        holder.itemView.getContext()
                                );

                        boolean deleted =
                                databaseHelper.deleteIngredient(
                                        ingredient.getId()
                                );

                        if (deleted) {

                            int currentPosition =
                                    holder.getAdapterPosition();

                            if (currentPosition !=
                                    RecyclerView.NO_POSITION) {

                                ingredientList.remove(currentPosition);

                                notifyItemRemoved(currentPosition);
                            }

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "Ingredient deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "Failed to delete ingredient",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class PantryItemViewHolder
            extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewQuantity;
        TextView textViewExpiry;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public PantryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName =
                    itemView.findViewById(
                            R.id.textViewIngredientName
                    );

            textViewQuantity =
                    itemView.findViewById(
                            R.id.textViewIngredientQuantity
                    );

            textViewExpiry =
                    itemView.findViewById(
                            R.id.textViewIngredientExpiry
                    );

            btnEditIngredient =
                    itemView.findViewById(
                            R.id.btnEditIngredient
                    );

            btnDeleteIngredient =
                    itemView.findViewById(
                            R.id.btnDeleteIngredient
                    );
        }
    }
}