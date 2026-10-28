
package com.example.mad700_assignment_tabassumbeemath;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
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

    private int pantryEntryCount = 0;
    private int recipeCount = 0;
    private int matchingRecipeCount = 0;

    private Map<String, Double> currentPantry = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pantryDatabase = new PantryDatabaseHelper(this);
        recipeDatabase = new RecipeDatabaseHelper(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 16);

        TextView heading = new TextView(this);
        heading.setText("All Recipes");
        heading.setTextSize(26);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, 8, 0, 16);
        layout.addView(heading);

        message = new TextView(this);
        message.setTextSize(15);
        message.setPadding(0, 8, 0, 12);
        layout.addView(message);

        ListView listView = new ListView(this);

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                recipeNames
        ) {
            @Override
            public android.view.View getView(
                    int position,
                    android.view.View convertView,
                    ViewGroup parent) {

                TextView row = (TextView) super.getView(
                        position, convertView, parent);

                row.setTextSize(16);
                row.setPadding(20, 18, 20, 18);

                return row;
            }
        };

        listView.setAdapter(adapter);

        layout.addView(listView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        ));

        Button backButton = new Button(this);
        backButton.setText("Back to Pantry");
        layout.addView(backButton);

        setContentView(layout);

        backButton.setOnClickListener(v -> finish());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < recipeIds.size()) {
                Intent intent = new Intent(
                        SuggestedRecipesActivity.this,
                        RecipeDetailActivity.class
                );

                intent.putExtra("recipe_id", recipeIds.get(position));
                startActivity(intent);
            }
        });

        loadSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pantryDatabase != null && recipeDatabase != null
                && adapter != null) {
            loadSuggestions();
        }
    }

    private void loadSuggestions() {
        recipeNames.clear();
        recipeIds.clear();

        currentPantry = loadPantryQuantities();

        try (Cursor pantryCursor =
                     pantryDatabase.getReadableDatabase().query(
                             PantryDatabaseHelper.TABLE_INGREDIENTS,
                             new String[]{"id"},
                             null, null, null, null, null)) {

            pantryEntryCount = pantryCursor.getCount();
        }

        recipeCount = 0;
        matchingRecipeCount = 0;

        try (Cursor recipes = recipeDatabase.getAllRecipes()) {

            while (recipes.moveToNext()) {
                recipeCount++;

                long recipeId = recipes.getLong(0);
                String recipeName = recipes.getString(1);

                String failure = getRecipeFailureReason(
                        recipeId, currentPantry);

                recipeIds.add(recipeId);

                if (failure == null) {
                    matchingRecipeCount++;

                    recipeNames.add(
                            "\u2713 " + recipeName
                                    + "\n   CAN MAKE WITH YOUR PANTRY"
                    );

                } else {
                    recipeNames.add(
                            "\u2022 " + recipeName
                                    + "\n   " + failure
                    );
                }
            }

        } catch (Exception e) {
            message.setText(
                    "Unable to load recipes.\n\n"
                            + e.getClass().getSimpleName()
                            + ": " + e.getMessage()
            );

            adapter.notifyDataSetChanged();
            return;
        }

        adapter.notifyDataSetChanged();

        message.setText(
                "Recipes in database: " + recipeCount
                        + "\nRecipes you can make now: "
                        + matchingRecipeCount
                        + "\nPantry entries saved: " + pantryEntryCount
                        + "\n\nTap any recipe to view its details."
        );

        if (recipeCount == 0) {
            message.append(
                    "\n\nNo recipes were found. "
                            + "Check the recipe database seeding."
            );
        }
    }

    private Map<String, Double> loadPantryQuantities() {
        Map<String, Double> pantry = new HashMap<>();

        try (Cursor cursor =
                     pantryDatabase.getReadableDatabase().query(
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

    // Returns null when all required ingredients and quantities are available.
    private String getRecipeFailureReason(
            long recipeId,
            Map<String, Double> pantry) {

        boolean hasIngredients = false;

        try (Cursor required =
                     recipeDatabase.getRecipeIngredients(recipeId)) {

            while (required.moveToNext()) {
                hasIngredients = true;

                String originalName = required.getString(0);
                double needed = required.getDouble(1);
                String originalUnit = required.getString(2);

                String name = normalize(originalName);
                String unit = normalizeUnit(originalUnit);
                String key = name + "|" + unit;

                Double available = pantry.get(key);

                if (available == null) {
                    return "Missing " + originalName
                            + " (requires " + needed + " "
                            + originalUnit + ")";
                }

                if (available + 0.000001 < needed) {
                    return "Not enough " + originalName
                            + " (needs " + needed + " "
                            + originalUnit + "; have "
                            + available + " " + originalUnit + ")";
                }
            }
        }

        if (!hasIngredients) {
            return "No ingredient records available";
        }

        return null;
    }

    private String normalize(String value) {
        String result = value == null
                ? ""
                : value.trim().toLowerCase(Locale.ROOT);

        if (result.endsWith("ies") && result.length() > 3) {
            result = result.substring(0, result.length() - 3) + "y";
        } else if (result.endsWith("s")
                && !result.endsWith("ss")
                && result.length() > 1) {
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

            case "slice":
                return "slices";

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
