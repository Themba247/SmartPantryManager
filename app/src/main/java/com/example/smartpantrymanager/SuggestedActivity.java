package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SuggestedRecipeAdapter adapter;
    private RecyclerView recyclerSuggested;
    private TextView tvNoMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);
        findViewById(R.id.btnBackSuggested).setOnClickListener(v -> finish());
        db = new DatabaseHelper(this);
        recyclerSuggested = findViewById(R.id.recyclerSuggested);
        tvNoMatches = findViewById(R.id.tvNoMatches);

        recyclerSuggested.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SuggestedRecipeAdapter(new java.util.ArrayList<>(), recipe -> {
            Intent intent = new Intent(SuggestedActivity.this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });
        recyclerSuggested.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<Recipe> allRecipes = db.getAllRecipes();
        List<PantryItem> pantry = db.getAllItems();
        List<Recipe> suggested = RecipeMatcher.getSuggestedRecipes(allRecipes, pantry);

        adapter.setRecipes(suggested);

        if (suggested.isEmpty()) {
            tvNoMatches.setVisibility(View.VISIBLE);
            recyclerSuggested.setVisibility(View.GONE);
        } else {
            tvNoMatches.setVisibility(View.GONE);
            recyclerSuggested.setVisibility(View.VISIBLE);
        }
    }
}