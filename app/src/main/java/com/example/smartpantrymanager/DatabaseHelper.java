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
        seedRecipes(db);
    }

    // Loads a starter set of recipes the first time the database is created
    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(db, "Tomato Scrambled Eggs",
                "1. Beat the eggs.\n2. Chop the tomato.\n3. Cook tomato in a pan for 2 minutes.\n" +
                        "4. Add eggs and stir until set.\n5. Season and serve.",
                new Object[][]{
                        {"egg", 3, "pcs"},
                        {"tomato", 2, "pcs"},
                        {"salt", 1, "tsp"},
                        {"oil", 1, "tbsp"}
                });

        addRecipe(db, "Garlic Butter Rice",
                "1. Melt butter in a pot.\n2. Add chopped garlic and fry until fragrant.\n" +
                        "3. Add rice and stock.\n4. Simmer 15 minutes until cooked.",
                new Object[][]{
                        {"rice", 200, "g"},
                        {"garlic", 3, "pcs"},
                        {"butter", 30, "g"},
                        {"vegetable stock", 400, "ml"}
                });

        addRecipe(db, "Simple Pasta Aglio e Olio",
                "1. Boil pasta until al dente.\n2. Fry sliced garlic in olive oil.\n" +
                        "3. Toss pasta with the garlic oil.\n4. Season and serve.",
                new Object[][]{
                        {"pasta", 200, "g"},
                        {"garlic", 4, "pcs"},
                        {"olive oil", 3, "tbsp"},
                        {"salt", 1, "tsp"}
                });

        addRecipe(db, "Cheese Omelette",
                "1. Beat the eggs.\n2. Pour into a hot, buttered pan.\n" +
                        "3. Sprinkle cheese on top.\n4. Fold and serve.",
                new Object[][]{
                        {"egg", 2, "pcs"},
                        {"cheese", 50, "g"},
                        {"butter", 10, "g"},
                        {"salt", 1, "tsp"}
                });

        addRecipe(db, "Banana Pancakes",
                "1. Mash the banana.\n2. Mix with flour, egg and milk into a batter.\n" +
                        "3. Cook spoonfuls on a hot pan until golden on both sides.",
                new Object[][]{
                        {"banana", 2, "pcs"},
                        {"flour", 150, "g"},
                        {"egg", 1, "pcs"},
                        {"milk", 200, "ml"}
                });

        addRecipe(db, "Tomato Soup",
                "1. Fry chopped onion until soft.\n2. Add chopped tomato and stock.\n" +
                        "3. Simmer 15 minutes.\n4. Blend until smooth and season.",
                new Object[][]{
                        {"tomato", 6, "pcs"},
                        {"onion", 1, "pcs"},
                        {"vegetable stock", 500, "ml"},
                        {"salt", 1, "tsp"}
                });

        addRecipe(db, "Chicken Fried Rice",
                "1. Cook chicken pieces until done.\n2. Add cooked rice and soy sauce.\n" +
                        "3. Stir-fry 5 minutes.\n4. Add chopped spring onion and serve.",
                new Object[][]{
                        {"chicken breast", 200, "g"},
                        {"rice", 300, "g"},
                        {"soy sauce", 2, "tbsp"},
                        {"spring onion", 2, "pcs"}
                });

        addRecipe(db, "Grilled Cheese Sandwich",
                "1. Butter the outside of two bread slices.\n2. Place cheese between them.\n" +
                        "3. Grill both sides in a pan until golden and melted.",
                new Object[][]{
                        {"bread", 2, "pcs"},
                        {"cheese", 60, "g"},
                        {"butter", 10, "g"}
                });

        addRecipe(db, "Vegetable Stir Fry",
                "1. Heat oil in a pan or wok.\n2. Add chopped vegetables.\n" +
                        "3. Stir-fry 5-7 minutes.\n4. Add soy sauce and serve.",
                new Object[][]{
                        {"carrot", 2, "pcs"},
                        {"broccoli", 150, "g"},
                        {"garlic", 2, "pcs"},
                        {"soy sauce", 2, "tbsp"}
                });

        addRecipe(db, "Potato Salad",
                "1. Boil the potatoes until soft.\n2. Cool and cube them.\n" +
                        "3. Mix with mayonnaise and chopped onion.\n4. Season and chill.",
                new Object[][]{
                        {"potato", 4, "pcs"},
                        {"mayonnaise", 3, "tbsp"},
                        {"onion", 1, "pcs"},
                        {"salt", 1, "tsp"}
                });

        addRecipe(db, "Egg Fried Rice",
                "1. Scramble the eggs in a hot pan.\n2. Add cooked rice.\n" +
                        "3. Stir-fry with soy sauce for 3-4 minutes.",
                new Object[][]{
                        {"egg", 2, "pcs"},
                        {"rice", 250, "g"},
                        {"soy sauce", 2, "tbsp"}
                });

        addRecipe(db, "Pancakes",
                "1. Mix flour, egg and milk into a smooth batter.\n" +
                        "2. Pour onto a hot pan.\n3. Cook until bubbles form, then flip.",
                new Object[][]{
                        {"flour", 200, "g"},
                        {"egg", 2, "pcs"},
                        {"milk", 300, "ml"}
                });

        addRecipe(db, "Carrot and Ginger Soup",
                "1. Fry chopped onion and ginger.\n2. Add chopped carrot and stock.\n" +
                        "3. Simmer 20 minutes.\n4. Blend until smooth.",
                new Object[][]{
                        {"carrot", 5, "pcs"},
                        {"ginger", 1, "pcs"},
                        {"onion", 1, "pcs"},
                        {"vegetable stock", 500, "ml"}
                });

        addRecipe(db, "Baked Beans on Toast",
                "1. Heat the baked beans in a pot.\n2. Toast the bread.\n" +
                        "3. Pour beans over toast and serve.",
                new Object[][]{
                        {"bread", 2, "pcs"},
                        {"baked beans", 400, "g"}
                });

        addRecipe(db, "Chicken Noodle Soup",
                "1. Cook chicken pieces in stock until done.\n" +
                        "2. Add noodles and simmer until soft.\n3. Season and serve.",
                new Object[][]{
                        {"chicken breast", 200, "g"},
                        {"noodles", 150, "g"},
                        {"vegetable stock", 600, "ml"}
                });

        addRecipe(db, "Fruit Salad",
                "1. Chop the banana and apple.\n2. Mix in a bowl.\n" +
                        "3. Add a squeeze of lemon juice and serve.",
                new Object[][]{
                        {"banana", 2, "pcs"},
                        {"apple", 2, "pcs"},
                        {"lemon", 1, "pcs"}
                });

        addRecipe(db, "Mashed Potatoes",
                "1. Boil the potatoes until soft.\n2. Mash with butter and milk.\n" +
                        "3. Season with salt and serve.",
                new Object[][]{
                        {"potato", 5, "pcs"},
                        {"butter", 30, "g"},
                        {"milk", 100, "ml"},
                        {"salt", 1, "tsp"}
                });

        addRecipe(db, "Onion and Cheese Toastie",
                "1. Fry the sliced onion until soft.\n2. Place onion and cheese between bread slices.\n" +
                        "3. Grill both sides until golden.",
                new Object[][]{
                        {"bread", 2, "pcs"},
                        {"cheese", 50, "g"},
                        {"onion", 1, "pcs"}
                });
    }

    // Inserts one recipe and its ingredient list
    private void addRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("steps", steps);
        long recipeId = db.insert("recipes", null, recipeValues);

        for (Object[] ing : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("name", (String) ing[0]);
            ingredientValues.put("quantity", ((Number) ing[1]).doubleValue());
            ingredientValues.put("unit", (String) ing[2]);
            db.insert("recipe_ingredients", null, ingredientValues);
        }
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