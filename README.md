# Smart Pantry Manager

Author: Themba Mabena
Student Number: 402309902
Module: Mobile Application Development 700 (MAD700D)

## Description

Smart Pantry Manager is an Android app that is designed to help users minimize the wasting of food by tracking the ingredients they have in their pantry at home and by suggesting recipes they can cook using only those ingredients.
A recipe is only suggested if all its ingredients are already in the pantry and that the ingredients meet the required quantity.

### Core Features

- Pantry management — add, edit, and delete ingredients (name, quantity, unit, and optional expiry date)
- Pantry list screen — view all current ingredients
- Suggested Recipes — see only the recipes you can make based on strict ingredient matching
- Recipe Detail — view full ingredients and preparation steps for a selected recipe
- Settings — toggle expiring-soon alerts and set a preferred unit system
- 18 pre-loaded recipes, added automatically on the first run


## Database

Choice: SQLite, via `SQLiteOpenHelper`.

I chose SQLite because I wanted the pantry to remain available without an internet connection. I’m building the app in Java and testing each feature in the Android emulator. The app focuses on saving pantry items and only suggesting recipes when all required ingredients are available.


The database has three tables:
- `pantry` — the user's current ingredients
- `recipes` — recipe names and preparation steps
- `recipe_ingredients` — each recipe's required ingredients, it is linked to `recipes` by `recipe_id`


## Setup / Run Instructions

1. Clone or download this repository.
2. Open the project folder in Android Studio (Tested with: Android Studio Quail 4 (2026.1.4 Patch 1) on Windows 11).
3. Let Gradle sync finish (this downloads the required libraries automatically).
4. Create or select an emulator via Tools → Device Manager (or connect a physical Android device with USB debugging enabled).
5. Click Run ▶ to build and launch the app.
6. On first launch, the database is created automatically and it is pre-loaded with 18 recipes, no further setup is needed.

## Technology

- Language: Java
- IDE: Android Studio
- Database: SQLite (`SQLiteOpenHelper`)
- UI: RecyclerView with custom adapters, ConstraintLayout, Material Components
- Font: Poppins (Google Fonts, open license)

## Repository

GitHub URL — https://github.com/Themba247/SmartPantryManager