package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private RecyclerView recyclerPantry;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                // TEMPORARY: will open the Edit screen in the next step
                Toast.makeText(MainActivity.this, "Tapped: " + item.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(PantryItem item) {
                // TEMPORARY: will become a delete confirmation in the next step
                db.deleteItem(item.getId());
                loadItems();
            }
        });
        recyclerPantry.setAdapter(adapter);

        // TEMPORARY: adds a sample item so we can test the list.
        // Will open the Add screen in the next step.
        fabAdd.setOnClickListener(v -> {
            int count = db.getAllItems().size() + 1;
            db.addItem(new PantryItem(0, "Sample item " + count, 2, "pcs", ""));
            loadItems();
        });
    }

    // Reload the list every time this screen comes back into view
    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        List<PantryItem> items = db.getAllItems();
        adapter.setItems(items);

        // Show the friendly message when there is nothing to display
        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerPantry.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerPantry.setVisibility(View.VISIBLE);
        }
    }
}