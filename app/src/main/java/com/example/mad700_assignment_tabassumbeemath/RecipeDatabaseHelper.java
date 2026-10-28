
package com.example.mad700_assignment_tabassumbeemath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_recipes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String RECIPES = "recipes";
    public static final String RECIPE_INGREDIENTS = "recipe_ingredients";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RECIPES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "method TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + RECIPE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, " +
                "ingredient_name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id))");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        // Keep existing recipes when the database version is unchanged.
    }

    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Tomato and Onion Pasta",
                "1. Boil pasta until tender.\n" +
                        "2. Chop and cook the onion and tomatoes.\n" +
                        "3. Mix together, season and serve.",
                new String[]{"pasta", "tomato", "onion"},
                new double[]{200, 2, 1},
                new String[]{"g", "items", "items"});

        addRecipe(db, "Potato Mash",
                "1. Peel and chop the potatoes.\n" +
                        "2. Boil until soft.\n" +
                        "3. Mash with butter and milk.",
                new String[]{"potato", "butter", "milk"},
                new double[]{500, 30, 50},
                new String[]{"g", "g", "ml"});

        addRecipe(db, "Egg Fried Rice",
                "1. Cook the rice and allow it to cool.\n" +
                        "2. Scramble the eggs in a pan.\n" +
                        "3. Add rice and oil, then stir-fry.",
                new String[]{"rice", "egg", "oil"},
                new double[]{200, 2, 15},
                new String[]{"g", "items", "ml"});

        addRecipe(db, "Vegetable Soup",
                "1. Chop the carrot, potato and onion.\n" +
                        "2. Simmer in water until tender.\n" +
                        "3. Season and serve warm.",
                new String[]{"carrot", "potato", "onion"},
                new double[]{2, 2, 1},
                new String[]{"items", "items", "items"});

        addRecipe(db, "Banana Pancakes",
                "1. Mash the banana.\n" +
                        "2. Mix with flour, milk and egg.\n" +
                        "3. Cook small pancakes in a lightly oiled pan.",
                new String[]{"banana", "flour", "milk", "egg"},
                new double[]{1, 100, 100, 1},
                new String[]{"items", "g", "ml", "items"});

        addRecipe(db, "Cheese Omelette",
                "1. Beat the eggs.\n" +
                        "2. Pour into a lightly oiled pan.\n" +
                        "3. Add cheese, fold and cook until set.",
                new String[]{"egg", "cheese", "oil"},
                new double[]{2, 30, 5},
                new String[]{"items", "g", "ml"});

        addRecipe(db, "Carrot Salad",
                "1. Wash and grate the carrots.\n" +
                        "2. Add lemon juice.\n" +
                        "3. Toss and serve.",
                new String[]{"carrot", "lemon"},
                new double[]{3, 1},
                new String[]{"items", "items"});

        addRecipe(db, "Garlic Butter Pasta",
                "1. Boil the pasta until tender.\n" +
                        "2. Melt butter and cook the garlic briefly.\n" +
                        "3. Toss the pasta in the garlic butter.",
                new String[]{"pasta", "butter", "garlic"},
                new double[]{200, 30, 2},
                new String[]{"g", "g", "cloves"});

        addRecipe(db, "Tomato Egg Scramble",
                "1. Chop and cook the tomatoes.\n" +
                        "2. Beat and add the eggs.\n" +
                        "3. Stir gently until the eggs are cooked.",
                new String[]{"tomato", "egg", "onion"},
                new double[]{2, 2, 1},
                new String[]{"items", "items", "items"});

        addRecipe(db, "Simple Rice and Beans",
                "1. Cook the rice until tender.\n" +
                        "2. Heat the cooked beans.\n" +
                        "3. Combine and season to taste.",
                new String[]{"rice", "beans", "onion"},
                new double[]{200, 200, 1},
                new String[]{"g", "g", "items"});

        addRecipe(db, "Potato and Onion Fry",
                "1. Slice the potatoes and onion.\n" +
                        "2. Heat oil in a pan.\n" +
                        "3. Fry until the potatoes are cooked through.",
                new String[]{"potato", "onion", "oil"},
                new double[]{400, 1, 20},
                new String[]{"g", "items", "ml"});

        addRecipe(db, "Apple Banana Fruit Bowl",
                "1. Wash and chop the apple.\n" +
                        "2. Peel and slice the banana.\n" +
                        "3. Combine and serve fresh.",
                new String[]{"apple", "banana"},
                new double[]{1, 1},
                new String[]{"items", "items"});

        addRecipe(db, "Garlic Vegetable Rice",
                "1. Cook the rice.\n" +
                        "2. Cook the carrot and garlic in a pan.\n" +
                        "3. Add rice and stir-fry until heated through.",
                new String[]{"rice", "carrot", "garlic", "oil"},
                new double[]{200, 1, 2, 15},
                new String[]{"g", "items", "cloves", "ml"});

        addRecipe(db, "Cheesy Mashed Potatoes",
                "1. Boil the potatoes until soft.\n" +
                        "2. Drain and mash with milk.\n" +
                        "3. Mix in the cheese and serve.",
                new String[]{"potato", "milk", "cheese"},
                new double[]{400, 50, 40},
                new String[]{"g", "ml", "g"});

        addRecipe(db, "Lemon Garlic Carrots",
                "1. Slice the carrots.\n" +
                        "2. Cook until tender.\n" +
                        "3. Add garlic and lemon juice, then serve.",
                new String[]{"carrot", "garlic", "lemon", "butter"},
                new double[]{3, 1, 1, 15},
                new String[]{"items", "cloves", "items", "g"});
    }

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String method,
            String[] names,
            double[] quantities,
            String[] units) {

        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("method", method);

        long recipeId = db.insert(RECIPES, null, recipeValues);

        if (recipeId == -1) {
            return;
        }

        for (int i = 0; i < names.length; i++) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", names[i]);
            ingredientValues.put("quantity", quantities[i]);
            ingredientValues.put("unit", units[i]);

            db.insert(RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }

    public Cursor getAllRecipes() {
        return getReadableDatabase().query(
                RECIPES,
                new String[]{"id", "name", "method"},
                null, null, null, null, "name ASC");
    }

    public Cursor getRecipeIngredients(long recipeId) {
        return getReadableDatabase().query(
                RECIPE_INGREDIENTS,
                new String[]{
                        "ingredient_name", "quantity", "unit"
                },
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, "ingredient_name ASC");
    }
}
