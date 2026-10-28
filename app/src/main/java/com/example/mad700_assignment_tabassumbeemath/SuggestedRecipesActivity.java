
package com.example.mad700_assignment_tabassumbeemath;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private PantryDatabaseHelper pantryDatabase;
    private RecipeDatabaseHelper recipeDatabase;

    private final ArrayList<String> recipeNames = new ArrayList<>();
    private final ArrayList<Long> recipeIds = new ArrayList<>();

    private ArrayAdapter<String> adapter;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pantryDatabase = new PantryDatabaseHelper(this);
        recipeDatabase = new RecipeDatabaseHelper(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 16);

        TextView heading = new TextView(this);
        heading.setText("Suggested Recipes");
        heading.setTextSize(26);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, 8, 0, 16);
        layout.addView(heading);

        message = new TextView(this);
        message.setTextSize(16);
        message.setPadding(0, 8, 0, 12);
        layout.addView(message);

        ListView listView = new ListView(this);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                recipeNames);

        listView.setAdapter(adapter);

        layout.addView(listView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        Button backButton = new Button(this);
        backButton.setText("Back to Pantry");
        layout.addView(backButton);

        setContentView(layout);

        backButton.setOnClickListener(v -> finish());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < recipeIds.size()) {
                Intent intent = new Intent(
                        SuggestedRecipesActivity.this,
                        RecipeDetailActivity.class);

                intent.putExtra("recipe_id", recipeIds.get(position));
                startActivity(intent);
            }
        });

        loadSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pantryDatabase != null && adapter != null) {
            loadSuggestions();
        }
    }

    private void loadSuggestions() {
        recipeNames.clear();
        recipeIds.clear();

        Map<String, Double> pantry = loadPantryQuantities();

        try (Cursor recipes = recipeDatabase.getAllRecipes()) {
            while (recipes.moveToNext()) {
                long recipeId = recipes.getLong(0);
                String recipeName = recipes.getString(1);

                if (canMakeRecipe(recipeId, pantry)) {
                    recipeIds.add(recipeId);
                    recipeNames.add(recipeName);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (recipeNames.isEmpty()) {
            message.setText(
                    "No recipes can be made with your current pantry. " +
                            "Add more ingredients or quantities and try again.");
        } else {
            message.setText(
                    recipeNames.size() +
                            " recipe(s) can be made with your current pantry. " +
                            "Tap a recipe to see its details.");
        }
    }

    private Map<String, Double> loadPantryQuantities() {
        Map<String, Double> pantry = new HashMap<>();

        try (Cursor cursor = pantryDatabase.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_INGREDIENTS,
                new String[]{"name", "quantity", "unit"},
                null, null, null, null, null)) {

            while (cursor.moveToNext()) {
                String name = normalize(cursor.getString(0));
                double quantity = cursor.getDouble(1);
                String unit = normalizeUnit(cursor.getString(2));

                String key = name + "|" + unit;
                double previous = pantry.containsKey(key)
                        ? pantry.get(key) : 0.0;

                pantry.put(key, previous + quantity);
            }
        }

        return pantry;
    }

    private boolean canMakeRecipe(
            long recipeId, Map<String, Double> pantry) {

        boolean hasIngredients = false;

        try (Cursor required =
                     recipeDatabase.getRecipeIngredients(recipeId)) {

            while (required.moveToNext()) {
                hasIngredients = true;

                String name = normalize(required.getString(0));
                double needed = required.getDouble(1);
                String unit = normalizeUnit(required.getString(2));

                String key = name + "|" + unit;
                Double available = pantry.get(key);

                if (available == null ||
                        available + 0.000001 < needed) {
                    return false;
                }
            }
        }

        return hasIngredients;
    }

    private String normalize(String value) {
        String result = value == null ? "" :
                value.trim().toLowerCase(Locale.ROOT);

        if (result.endsWith("ies") && result.length() > 3) {
            result = result.substring(0, result.length() - 3) + "y";
        } else if (result.endsWith("s") &&
                !result.endsWith("ss") && result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    private String normalizeUnit(String value) {
        String unit = normalize(value);

        switch (unit) {
            case "gram":
                return "g";
            case "kilogram":
                return "kg";
            case "milliliter":
            case "millilitre":
                return "ml";
            case "liter":
            case "litre":
                return "l";
            case "piece":
            case "item":
                return "items";
            case "clove":
                return "cloves";
            case "cup":
                return "cups";
            default:
                return unit;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pantryDatabase != null) {
            pantryDatabase.close();
        }

        if (recipeDatabase != null) {
            recipeDatabase.close();
        }
    }
}
