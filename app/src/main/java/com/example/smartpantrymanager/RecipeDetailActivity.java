package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());
        DatabaseHelper db = new DatabaseHelper(this);
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = db.getRecipeById(recipeId);

        if (recipe == null) {
            finish();
            return;
        }

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvSteps = findViewById(R.id.tvDetailSteps);

        tvName.setText(recipe.getName());

        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            ingredientsText.append("• ")
                    .append(formatQuantity(ing.getQuantity()))
                    .append(" ")
                    .append(ing.getUnit())
                    .append(" ")
                    .append(ing.getName())
                    .append("\n");
        }
        tvIngredients.setText(ingredientsText.toString().trim());

        tvSteps.setText(recipe.getSteps());
    }

    private String formatQuantity(double q) {
        if (q == (long) q) {
            return String.valueOf((long) q);
        }
        return String.valueOf(q);
    }
}