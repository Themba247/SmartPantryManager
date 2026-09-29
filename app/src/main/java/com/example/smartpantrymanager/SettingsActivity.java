package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "pantry_prefs";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    public static final String KEY_UNIT_PREFERENCE = "unit_preference";

    private final String[] unitOptions = {"Metric (g, ml)", "Imperial (oz, cups)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.btnBackSettings).setOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Switch switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        Spinner spinnerUnitPreference = findViewById(R.id.spinnerUnitPreference);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, unitOptions);
        spinnerUnitPreference.setAdapter(unitAdapter);

        // Load saved values (defaults: alerts on, metric selected)
        switchExpiryAlerts.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));
        spinnerUnitPreference.setSelection(prefs.getInt(KEY_UNIT_PREFERENCE, 0));

        // Save immediately whenever the user changes something
        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());

        spinnerUnitPreference.post(() -> spinnerUnitPreference.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        prefs.edit().putInt(KEY_UNIT_PREFERENCE, position).apply();
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                }));
    }
}