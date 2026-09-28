package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private RecyclerView recyclerPantry;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        recyclerPantry = findViewById(R.id.recyclerPantry);
        tvEmpty = findViewById(R.id.tvEmpty);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PantryAdapter(new ArrayList<>(), new PantryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PantryItem item) {
                // Open the Add/Edit screen and pass this item's id (edit mode)
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_ITEM_ID, item.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(PantryItem item) {
                confirmDelete(item);
            }
        });
        recyclerPantry.setAdapter(adapter);

        // Open the Add/Edit screen with no id (add mode)
        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddEditActivity.class)));
    }

    // Reload the list every time this screen comes back into view
    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void confirmDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Delete " + item.getName() + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteItem(item.getId());
                    loadItems();
                    Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadItems() {
        List<PantryItem> items = db.getAllItems();
        adapter.setItems(items);

        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerPantry.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerPantry.setVisibility(View.VISIBLE);
        }
    }
}