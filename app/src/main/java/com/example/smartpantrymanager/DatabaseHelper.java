package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Runs once, the first time the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiry_date TEXT)");

        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "steps TEXT NOT NULL)");

        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    // ---------- CREATE ----------
    public long addItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", item.getExpiryDate());
        return db.insert("pantry", null, values);
    }

    // ---------- READ (all) ----------
    public List<PantryItem> getAllItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pantry ORDER BY name COLLATE NOCASE", null);
        try {
            while (cursor.moveToNext()) {
                items.add(cursorToItem(cursor));
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    // ---------- READ (one) ----------
    public PantryItem getItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pantry WHERE id = ?",
                new String[]{String.valueOf(id)});
        try {
            if (cursor.moveToFirst()) {
                return cursorToItem(cursor);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // ---------- UPDATE ----------
    public int updateItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", item.getExpiryDate());
        return db.update("pantry", values, "id = ?",
                new String[]{String.valueOf(item.getId())});
    }

    // ---------- DELETE ----------
    public int deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("pantry", "id = ?", new String[]{String.valueOf(id)});
    }

    // Helper: turns one database row into a PantryItem object
    private PantryItem cursorToItem(Cursor c) {
        return new PantryItem(
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getDouble(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("unit")),
                c.getString(c.getColumnIndexOrThrow("expiry_date")));
    }
}