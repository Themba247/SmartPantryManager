package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    // The key used to pass the item's id from the list screen to this screen
    public static final String EXTRA_ITEM_ID = "item_id";

    private final String[] units = {"pcs", "g", "kg", "ml", "l", "tsp", "tbsp", "cup"};

    private DatabaseHelper db;
    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private int itemId = -1; // -1 means "adding a new item"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);

        TextView tvHeading = findViewById(R.id.tvHeading);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnClearDate = findViewById(R.id.btnClearDate);

        spinnerUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, units));

        etExpiry.setOnClickListener(v -> showDatePicker());
        btnClearDate.setOnClickListener(v -> etExpiry.setText(""));
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveItem());

        // Did the list screen send us an item id? If so, we are editing.
        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            tvHeading.setText("Edit Ingredient");
            loadItem();
        }
    }

    // Fill the form with the existing item's data
    private void loadItem() {
        PantryItem item = db.getItem(itemId);
        if (item == null) {
            finish();
            return;
        }
        etName.setText(item.getName());

        double q = item.getQuantity();
        etQuantity.setText(q == (long) q ? String.valueOf((long) q) : String.valueOf(q));

        etExpiry.setText(item.getExpiryDate());

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(item.getUnit())) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> etExpiry.setText(
                        String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText().toString().trim();

        // ----- Input validation -----
        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            etName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            etQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than 0");
            etQuantity.requestFocus();
            return;
        }

        // ----- Save -----
        if (itemId == -1) {
            db.addItem(new PantryItem(0, name, quantity, unit, expiry));
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateItem(new PantryItem(itemId, name, quantity, unit, expiry));
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish(); // go back to the list
    }
}