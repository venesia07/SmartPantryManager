package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        public PantryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName =
                    itemView.findViewById(R.id.textViewIngredientName);

            textViewQuantity =
                    itemView.findViewById(R.id.textViewIngredientQuantity);

            textViewExpiry =
                    itemView.findViewById(R.id.textViewIngredientExpiry);
        }
    }
}