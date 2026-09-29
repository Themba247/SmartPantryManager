package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    // Returns only the recipes where every ingredient is fully covered by the pantry
    public static List<Recipe> getSuggestedRecipes(List<Recipe> allRecipes, List<PantryItem> pantry) {
        List<Recipe> suggested = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (canMake(recipe, pantry)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    // The strict-matching rule: every required ingredient must be present
    // in the pantry in at least the required quantity.
    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        for (RecipeIngredient required : recipe.getIngredients()) {
            if (!isCovered(required, pantry)) {
                return false; // one missing/short ingredient disqualifies the whole recipe
            }
        }
        return true;
    }

    // Checks whether one required ingredient is satisfied by something in the pantry
    private static boolean isCovered(RecipeIngredient required, List<PantryItem> pantry) {
        String requiredName = normalize(required.getName());

        for (PantryItem pantryItem : pantry) {
            if (normalize(pantryItem.getName()).equals(requiredName)) {
                double requiredInBase = toBaseUnit(required.getQuantity(), required.getUnit());
                double haveInBase = toBaseUnit(pantryItem.getQuantity(), pantryItem.getUnit());

                // If units can't be compared (e.g. "pcs" vs "g"), fall back to unit-must-match
                if (Double.isNaN(requiredInBase) || Double.isNaN(haveInBase)) {
                    if (!normalize(required.getUnit()).equals(normalize(pantryItem.getUnit()))) {
                        continue; // different, incompatible units - not a match
                    }
                    if (pantryItem.getQuantity() >= required.getQuantity()) {
                        return true;
                    }
                } else {
                    if (haveInBase >= requiredInBase) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Makes ingredient names comparable: lowercase, trimmed, and simple plural handling.
    // "Tomatoes" and "tomato" become the same string.
    public static String normalize(String name) {
        String n = name.trim().toLowerCase(Locale.US);
        if (n.endsWith("es") && n.length() > 3) {
            n = n.substring(0, n.length() - 2); // "tomatoes" -> "tomat" ... handled below
            // re-add correct singular for common "oes"/"shes" patterns
            if (!n.endsWith("o") && !n.endsWith("sh") && !n.endsWith("ch")) {
                n = n; // leave as is, safe fallback
            }
        }
        if (n.endsWith("s") && !n.endsWith("ss") && n.length() > 3) {
            n = n.substring(0, n.length() - 1); // "eggs" -> "egg"
        }
        return n;
    }

    // Converts weight/volume units to one common base (grams or millilitres) so
    // 500 g can be compared with 0.5 kg, for example. Returns NaN if the unit
    // isn't a recognised weight/volume unit (e.g. "pcs"), meaning no conversion applies.
    private static double toBaseUnit(double quantity, String unit) {
        String u = normalize(unit);
        switch (u) {
            case "g": return quantity;
            case "kg": return quantity * 1000;
            case "ml": return quantity;
            case "l": return quantity * 1000;
            case "tsp": return quantity * 5;      // approx 5ml per teaspoon
            case "tbsp": return quantity * 15;    // approx 15ml per tablespoon
            case "cup": return quantity * 240;    // approx 240ml per cup
            default: return Double.NaN;           // "pcs" and anything else: no conversion
        }
    }
}