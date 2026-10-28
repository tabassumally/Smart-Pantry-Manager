
package com.example.mad700_assignment_tabassumbeemath;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivityJava extends AppCompatActivity {

    private PantryDatabaseHelper database;

    private EditText nameInput, quantityInput, unitInput, expiryInput;
    private EditText searchInput;
    private ArrayAdapter<String> adapter;
    private TextView expiryWarning;
    private ListView pantryList;

    private final ArrayList<String> ingredients = new ArrayList<>();
    private final ArrayList<Long> ingredientIds = new ArrayList<>();

    private final ArrayList<String> allIngredients = new ArrayList<>();
    private final ArrayList<Long> allIngredientIds = new ArrayList<>();

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String EXPIRY_ALERTS_KEY = "expiry_alerts_enabled";

    private int color(String name) {
        switch (name) {
            case "terracotta":
                return ContextCompat.getColor(this, R.color.terracotta);
            case "terracotta_dark":
                return ContextCompat.getColor(this, R.color.terracotta_dark);
            case "oat":
                return ContextCompat.getColor(this, R.color.oat);
            case "espresso":
                return ContextCompat.getColor(this, R.color.espresso);
            case "muted_brown":
                return ContextCompat.getColor(this, R.color.muted_brown);
            case "soft_border":
                return ContextCompat.getColor(this, R.color.soft_border);
            case "soft_red":
                return ContextCompat.getColor(this, R.color.soft_red);
            case "soft_green":
                return ContextCompat.getColor(this, R.color.soft_green);
            case "card_white":
                return ContextCompat.getColor(this, R.color.card_white);
            default:
                return ContextCompat.getColor(this, R.color.warm_ivory);
        }
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable roundedBackground(int backgroundColor, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(backgroundColor);
        drawable.setCornerRadius(dp(16));

        if (strokeColor != Color.TRANSPARENT) {
            drawable.setStroke(dp(1), strokeColor);
        }

        return drawable;
    }

    private LinearLayout verticalLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private LinearLayout.LayoutParams margins(
            int width, int height, int bottom) {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);

        params.bottomMargin = dp(bottom);
        return params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new PantryDatabaseHelper(this);

        getWindow().setStatusBarColor(color("warm_ivory"));
        getWindow().setNavigationBarColor(color("warm_ivory"));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        );

        LinearLayout root = verticalLayout();
        root.setBackgroundColor(color("warm_ivory"));
        root.setPadding(dp(20), dp(12), dp(20), dp(8));

        // Brand heading
        TextView heading = new TextView(this);
        heading.setText("Smart Pantry");
        heading.setTextSize(28);
        heading.setTextColor(color("espresso"));
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, dp(8), 0, dp(2));
        root.addView(heading, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0));

        TextView tagline = new TextView(this);
        tagline.setText("Make the most of what you have.");
        tagline.setTextSize(14);
        tagline.setTextColor(color("muted_brown"));
        tagline.setGravity(Gravity.CENTER);
        tagline.setPadding(0, 0, 0, dp(16));
        root.addView(tagline, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 12));

        // Expiry warning card
        expiryWarning = new TextView(this);
        expiryWarning.setTextSize(13);
        expiryWarning.setTextColor(color("espresso"));
        expiryWarning.setPadding(dp(14), dp(12), dp(14), dp(12));
        expiryWarning.setBackground(
                roundedBackground(color("soft_red"), Color.TRANSPARENT));
        expiryWarning.setVisibility(View.GONE);

        root.addView(expiryWarning, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 12));

        // Main navigation buttons
        LinearLayout navigationRow = new LinearLayout(this);
        navigationRow.setOrientation(LinearLayout.HORIZONTAL);
        navigationRow.setGravity(Gravity.CENTER_VERTICAL);

        Button recipesButton = createButton("Find Recipes", true);
        Button settingsButton = createButton("Settings", false);

        LinearLayout.LayoutParams navParams = new LinearLayout.LayoutParams(
                0, dp(48), 1);

        navParams.setMargins(0, 0, dp(8), 0);
        navigationRow.addView(recipesButton, navParams);

        LinearLayout.LayoutParams settingsParams =
                new LinearLayout.LayoutParams(0, dp(48), 1);
        settingsParams.setMargins(dp(8), 0, 0, 0);
        navigationRow.addView(settingsButton, settingsParams);

        root.addView(navigationRow, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 18));

        // Pantry section heading
        TextView pantryHeading = new TextView(this);
        pantryHeading.setText("My Pantry");
        pantryHeading.setTextSize(21);
        pantryHeading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        pantryHeading.setTextColor(color("espresso"));

        root.addView(pantryHeading, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 4));

        TextView pantrySubtitle = new TextView(this);
        pantrySubtitle.setText("Keep track of your ingredients");
        pantrySubtitle.setTextSize(13);
        pantrySubtitle.setTextColor(color("muted_brown"));

        root.addView(pantrySubtitle, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 12));

        // Ingredient form card
        LinearLayout formCard = verticalLayout();
        formCard.setPadding(dp(14), dp(12), dp(14), dp(14));
        formCard.setBackground(
                roundedBackground(color("oat"), color("soft_border")));

        nameInput = createInput("Ingredient name");
        formCard.addView(nameInput, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50), 8));

        quantityInput = createInput("Quantity (e.g. 2)");
        quantityInput.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        formCard.addView(quantityInput, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50), 8));

        unitInput = createInput("Unit (e.g. kg, g, cups, items)");
        formCard.addView(unitInput, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50), 8));

        expiryInput = createInput("Expiry date (optional: YYYY-MM-DD)");
        formCard.addView(expiryInput, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50), 12));

        Button addButton = createButton("+  Add Ingredient", true);
        formCard.addView(addButton, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(48)));

        root.addView(formCard, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 16));

        // Search row
        LinearLayout searchRow = new LinearLayout(this);
        searchRow.setOrientation(LinearLayout.HORIZONTAL);
        searchRow.setGravity(Gravity.CENTER_VERTICAL);

        searchInput = createInput("Search ingredients...");
        searchInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        LinearLayout.LayoutParams searchInputParams =
                new LinearLayout.LayoutParams(0, dp(50), 1);
        searchInputParams.setMargins(0, 0, dp(8), 0);
        searchRow.addView(searchInput, searchInputParams);

        Button searchButton = createButton("Search", false);
        searchRow.addView(searchButton, new LinearLayout.LayoutParams(
                dp(92), dp(50)));

        root.addView(searchRow, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 12));

        TextView listHint = new TextView(this);
        listHint.setText("Tap an ingredient to edit it  •  Hold to delete");
        listHint.setTextSize(12);
        listHint.setTextColor(color("muted_brown"));

        root.addView(listHint, margins(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 8));

        // Pantry list with a custom adapter
        pantryList = new ListView(this);
        pantryList.setDividerHeight(dp(8));
        pantryList.setDivider(null);
        pantryList.setBackgroundColor(Color.TRANSPARENT);
        pantryList.setPadding(0, 0, 0, dp(4));
        pantryList.setClipToPadding(false);

        adapter = new PantryAdapter();
        pantryList.setAdapter(adapter);

        LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);

        root.addView(pantryList, listParams);

        setContentView(root);

        // Preserve navigation
        recipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivityJava.this,
                    SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivityJava.this,
                    SettingsActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> addIngredient());
        searchButton.setOnClickListener(v -> performSearch());

        searchInput.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch();
                return true;
            }
            return false;
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                filterIngredients(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pantryList.setOnItemClickListener((parent, view, position, id) -> {
            if (position < ingredientIds.size()) {
                editIngredient(ingredientIds.get(position));
            }
        });

        pantryList.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    if (position < ingredientIds.size()) {
                        confirmDelete(
                                ingredientIds.get(position),
                                ingredients.get(position));
                    }
                    return true;
                });

        loadIngredients();
    }

    private Button createButton(String text, boolean primary) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(12), dp(8), dp(12), dp(8));
        button.setMinHeight(dp(48));
        button.setStateListAnimator(null);

        if (primary) {
            button.setTextColor(Color.WHITE);
            button.setBackground(roundedBackground(
                    color("terracotta"), Color.TRANSPARENT));
        } else {
            button.setTextColor(color("terracotta_dark"));
            button.setBackground(roundedBackground(
                    color("card_white"), color("soft_border")));
        }

        return button;
    }

    private EditText createInput(String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setSingleLine(true);
        input.setTextSize(14);
        input.setTextColor(color("espresso"));
        input.setHintTextColor(color("muted_brown"));
        input.setPadding(dp(14), dp(8), dp(14), dp(8));
        input.setBackground(roundedBackground(
                color("card_white"), color("soft_border")));
        input.setSelectAllOnFocus(false);
        return input;
    }

    private void performSearch() {
        filterIngredients(searchInput.getText().toString());

        InputMethodManager keyboard = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (keyboard != null) {
            keyboard.hideSoftInputFromWindow(
                    searchInput.getWindowToken(), 0);
        }

        searchInput.clearFocus();

        Toast.makeText(
                this,
                ingredients.size() + " matching ingredient(s)",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (database != null && adapter != null) {
            loadIngredients();
        }
    }

    private void addIngredient() {
        String name = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();
        String expiry = expiryInput.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
            showMessage("Enter the name, quantity and unit.");
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showMessage("Enter a valid quantity.");
            return;
        }

        if (!Double.isFinite(quantity) || quantity <= 0) {
            showMessage("Quantity must be greater than zero.");
            return;
        }

        if (!validExpiry(expiry)) {
            showMessage("Use a real date in YYYY-MM-DD format.");
            return;
        }

        long result = database.addIngredient(
                name, quantity, unit, emptyToNull(expiry));

        if (result != -1) {
            showMessage("Ingredient saved!");
            nameInput.setText("");
            quantityInput.setText("");
            unitInput.setText("");
            expiryInput.setText("");
            loadIngredients();
        } else {
            showMessage("Could not save ingredient.");
        }
    }

    private void loadIngredients() {
        allIngredients.clear();
        allIngredientIds.clear();

        try (Cursor cursor = database.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_INGREDIENTS,
                new String[]{
                        "id", "name", "quantity", "unit", "expiry_date"
                },
                null, null, null, null, "name ASC")) {

            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                double quantity = cursor.getDouble(2);
                String unit = cursor.getString(3);
                String expiry = cursor.getString(4);

                String item = name + " — " + quantity + " " + unit;

                if (expiry != null && !expiry.isEmpty()) {
                    item += "\nExpires: " + expiry;
                }

                allIngredientIds.add(id);
                allIngredients.add(item);
            }
        }

        filterIngredients(
                searchInput == null
                        ? ""
                        : searchInput.getText().toString());

        updateExpiryWarnings();
    }

    private void filterIngredients(String searchText) {
        ingredients.clear();
        ingredientIds.clear();

        String query = searchText.trim().toLowerCase(Locale.ROOT);

        for (int i = 0; i < allIngredients.size(); i++) {
            String item = allIngredients.get(i);

            if (item.toLowerCase(Locale.ROOT).contains(query)) {
                ingredients.add(item);
                ingredientIds.add(allIngredientIds.get(i));
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateExpiryWarnings() {
        if (expiryWarning == null) {
            return;
        }

        SharedPreferences preferences = getSharedPreferences(
                PREFS_NAME, MODE_PRIVATE);

        boolean alertsEnabled = preferences.getBoolean(
                EXPIRY_ALERTS_KEY, true);

        if (!alertsEnabled) {
            expiryWarning.setVisibility(View.GONE);
            return;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar threeDaysFromNow = (Calendar) today.clone();
        threeDaysFromNow.add(Calendar.DAY_OF_YEAR, 3);

        ArrayList<String> expired = new ArrayList<>();
        ArrayList<String> expiringSoon = new ArrayList<>();

        try (Cursor cursor = database.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_INGREDIENTS,
                new String[]{"name", "expiry_date"},
                "expiry_date IS NOT NULL AND expiry_date != ''",
                null, null, null, "expiry_date ASC")) {

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String expiryText = cursor.getString(1);
                Date expiryDate = parseExpiryDate(expiryText);

                if (expiryDate == null) {
                    continue;
                }

                Calendar expiryCalendar = Calendar.getInstance();
                expiryCalendar.setTime(expiryDate);
                expiryCalendar.set(Calendar.HOUR_OF_DAY, 0);
                expiryCalendar.set(Calendar.MINUTE, 0);
                expiryCalendar.set(Calendar.SECOND, 0);
                expiryCalendar.set(Calendar.MILLISECOND, 0);

                Date normalizedExpiry = expiryCalendar.getTime();

                if (normalizedExpiry.before(today.getTime())) {
                    expired.add(name + " (expired " + expiryText + ")");
                } else if (!normalizedExpiry.after(
                        threeDaysFromNow.getTime())) {
                    expiringSoon.add(name + " (expires " + expiryText + ")");
                }
            }
        }

        StringBuilder message = new StringBuilder();

        if (!expired.isEmpty()) {
            message.append("EXPIRED INGREDIENTS\n");
            for (String item : expired) {
                message.append("• ").append(item).append("\n");
            }
        }

        if (!expiringSoon.isEmpty()) {
            if (message.length() > 0) {
                message.append("\n");
            }

            message.append("EXPIRING WITHIN 3 DAYS\n");
            for (String item : expiringSoon) {
                message.append("• ").append(item).append("\n");
            }
        }

        if (message.length() == 0) {
            expiryWarning.setVisibility(View.GONE);
            return;
        }

        expiryWarning.setText(message.toString().trim());
        expiryWarning.setTextColor(color("terracotta_dark"));
        expiryWarning.setBackground(roundedBackground(
                color("soft_red"), color("soft_border")));
        expiryWarning.setVisibility(View.VISIBLE);
    }

    private Date parseExpiryDate(String dateText) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        format.setLenient(false);

        ParsePosition position = new ParsePosition(0);
        Date parsed = format.parse(dateText, position);

        if (parsed == null || position.getIndex() != dateText.length()) {
            return null;
        }

        return parsed;
    }

    private void editIngredient(long id) {
        String oldName;
        String oldQuantity;
        String oldUnit;
        String oldExpiry;

        try (Cursor cursor = database.getReadableDatabase().query(
                PantryDatabaseHelper.TABLE_INGREDIENTS,
                new String[]{"name", "quantity", "unit", "expiry_date"},
                "id = ?", new String[]{String.valueOf(id)},
                null, null, null)) {

            if (!cursor.moveToFirst()) {
                showMessage("Ingredient not found.");
                loadIngredients();
                return;
            }

            oldName = cursor.getString(0);
            oldQuantity = String.valueOf(cursor.getDouble(1));
            oldUnit = cursor.getString(2);
            oldExpiry = cursor.getString(3);
        }

        LinearLayout form = verticalLayout();
        form.setPadding(dp(20), dp(8), dp(20), 0);

        EditText name = createInput("Ingredient name");
        name.setText(oldName);
        form.addView(name, margins(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50), 8));

        EditText quantity = createInput("Quantity");
        quantity.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        quantity.setText(oldQuantity);
        form.addView(quantity, margins(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50), 8));

        EditText unit = createInput("Unit");
        unit.setText(oldUnit);
        form.addView(unit, margins(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50), 8));

        EditText expiry = createInput("Expiry date (YYYY-MM-DD)");
        expiry.setText(oldExpiry == null ? "" : oldExpiry);
        form.addView(expiry, margins(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50), 0));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Ingredient")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save Changes", null)
                .create();

        dialog.setOnShowListener(ignored -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(color("terracotta"));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(color("muted_brown"));

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {
                        String newName = name.getText().toString().trim();
                        String quantityText =
                                quantity.getText().toString().trim();
                        String newUnit = unit.getText().toString().trim();
                        String newExpiry = expiry.getText().toString().trim();

                        if (newName.isEmpty() || quantityText.isEmpty()
                                || newUnit.isEmpty()) {
                            showMessage(
                                    "Name, quantity and unit are required.");
                            return;
                        }

                        double newQuantity;

                        try {
                            newQuantity = Double.parseDouble(quantityText);
                        } catch (NumberFormatException e) {
                            showMessage("Enter a valid quantity.");
                            return;
                        }

                        if (!Double.isFinite(newQuantity)
                                || newQuantity <= 0) {
                            showMessage(
                                    "Quantity must be greater than zero.");
                            return;
                        }

                        if (!validExpiry(newExpiry)) {
                            showMessage(
                                    "Use a real date in YYYY-MM-DD format.");
                            return;
                        }

                        int updated = database.updateIngredient(
                                id, newName, newQuantity, newUnit,
                                emptyToNull(newExpiry));

                        if (updated > 0) {
                            showMessage("Ingredient updated!");
                            dialog.dismiss();
                            loadIngredients();
                        } else {
                            showMessage("No changes were saved.");
                        }
                    });
        });

        dialog.show();
    }

    private void confirmDelete(long id, String displayText) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete ingredient?")
                .setMessage("Remove this ingredient?\n\n" + displayText)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (ignored, which) -> {
                    int deleted = database.deleteIngredient(id);

                    if (deleted > 0) {
                        showMessage("Ingredient deleted.");
                        loadIngredients();
                    } else {
                        showMessage("Could not delete ingredient.");
                    }
                })
                .create();

        dialog.setOnShowListener(ignored -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(color("terracotta"));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(color("muted_brown"));
        });

        dialog.show();
    }

    private boolean validExpiry(String expiry) {
        return expiry.isEmpty() || parseExpiryDate(expiry) != null;
    }

    private String emptyToNull(String value) {
        return value.isEmpty() ? null : value;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Custom adapter for styled pantry ingredient rows.
    private class PantryAdapter extends ArrayAdapter<String> {

        PantryAdapter() {
            super(MainActivityJava.this,
                    android.R.layout.simple_list_item_1, ingredients);
        }

        @Override
        public View getView(int position, View convertView,
                            android.view.ViewGroup parent) {

            TextView itemView;

            if (convertView instanceof TextView) {
                itemView = (TextView) convertView;
            } else {
                itemView = new TextView(MainActivityJava.this);
            }

            itemView.setText(getItem(position));
            itemView.setTextSize(14);
            itemView.setTextColor(color("espresso"));
            itemView.setGravity(Gravity.CENTER_VERTICAL);
            itemView.setPadding(dp(16), dp(12), dp(16), dp(12));
            itemView.setMinHeight(dp(58));
            itemView.setMaxLines(3);
            itemView.setLineSpacing(dp(2), 1.0f);
            itemView.setBackground(roundedBackground(
                    color("card_white"), color("soft_border")));

            return itemView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (database != null) {
            database.close();
        }
    }
}
