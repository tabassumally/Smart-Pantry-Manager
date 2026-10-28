
package com.example.mad700_assignment_tabassumbeemath;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS = "ingredients";

    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiry_date TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        // Preserve existing pantry data during future upgrades.
    }

    public long addIngredient(
            String name, double quantity, String unit, String expiryDate) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        return getWritableDatabase().insert(
                TABLE_INGREDIENTS, null, values);
    }

    public int updateIngredient(
            long id, String name, double quantity,
            String unit, String expiryDate) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        return getWritableDatabase().update(
                TABLE_INGREDIENTS,
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteIngredient(long id) {
        return getWritableDatabase().delete(
                TABLE_INGREDIENTS,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }
}
