
package com.example.mad700_assignment_tabassumbeemath;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeDatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new RecipeDatabaseHelper(this);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);

        ScrollView scrollView = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 24);

        TextView heading = new TextView(this);
        heading.setText("Recipe Details");
        heading.setTextSize(26);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, 8, 0, 24);
        layout.addView(heading);

        TextView recipeName = new TextView(this);
        recipeName.setTextSize(22);
        recipeName.setPadding(0, 8, 0, 16);
        layout.addView(recipeName);

        TextView ingredientsHeading = new TextView(this);
        ingredientsHeading.setText("Ingredients");
        ingredientsHeading.setTextSize(20);
        ingredientsHeading.setPadding(0, 12, 0, 8);
        layout.addView(ingredientsHeading);

        TextView ingredientsText = new TextView(this);
        ingredientsText.setTextSize(16);
        ingredientsText.setPadding(0, 0, 0, 20);
        layout.addView(ingredientsText);

        TextView methodHeading = new TextView(this);
        methodHeading.setText("Preparation Method");
        methodHeading.setTextSize(20);
        methodHeading.setPadding(0, 12, 0, 8);
        layout.addView(methodHeading);

        TextView methodText = new TextView(this);
        methodText.setTextSize(16);
        layout.addView(methodText);

        Button backButton = new Button(this);
        backButton.setText("Back to Suggested Recipes");
        backButton.setOnClickListener(v -> finish());

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.topMargin = 24;

        layout.addView(backButton, buttonParams);

        scrollView.addView(layout);
        setContentView(scrollView);

        if (recipeId == -1) {
            recipeName.setText("Recipe not found");
            ingredientsText.setText(
                    "No recipe was selected.");
            methodText.setText("");
            return;
        }

        loadRecipe(recipeId, recipeName, ingredientsText, methodText);
    }

    private void loadRecipe(
            long recipeId,
            TextView recipeName,
            TextView ingredientsText,
            TextView methodText) {

        boolean found = false;

        try (Cursor cursor = database.getReadableDatabase().query(
                RecipeDatabaseHelper.RECIPES,
                new String[]{"name", "method"},
                "id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                found = true;
                recipeName.setText(cursor.getString(0));
                methodText.setText(cursor.getString(1));
            }
        }

        if (!found) {
            recipeName.setText("Recipe not found");
            ingredientsText.setText(
                    "This recipe could not be found.");
            methodText.setText("");
            return;
        }

        StringBuilder ingredientList = new StringBuilder();

        try (Cursor cursor =
                     database.getRecipeIngredients(recipeId)) {

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                double quantity = cursor.getDouble(1);
                String unit = cursor.getString(2);

                ingredientList.append("\u2022 ")
                        .append(name)
                        .append(" — ")
                        .append(quantity)
                        .append(" ")
                        .append(unit)
                        .append("\n\n");
            }
        }

        ingredientsText.setText(ingredientList.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (database != null) {
            database.close();
        }
    }
}
