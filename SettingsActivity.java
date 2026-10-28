
package com.example.mad700_assignment_tabassumbeemath;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String EXPIRY_ALERTS_KEY = "expiry_alerts_enabled";

    private SharedPreferences preferences;
    private Switch expirySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(
                PREFS_NAME, MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 24);

        TextView heading = new TextView(this);
        heading.setText("Settings");
        heading.setTextSize(28);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, 12, 0, 24);
        layout.addView(heading);

        TextView sectionHeading = new TextView(this);
        sectionHeading.setText("Expiry Notifications");
        sectionHeading.setTextSize(20);
        sectionHeading.setPadding(0, 12, 0, 8);
        layout.addView(sectionHeading);

        TextView description = new TextView(this);
        description.setText(
                "Choose whether you want expiry reminders " +
                        "for ingredients in your pantry.");
        description.setTextSize(16);
        description.setPadding(0, 0, 0, 16);
        layout.addView(description);

        expirySwitch = new Switch(this);
        expirySwitch.setText("Enable expiry reminders");
        expirySwitch.setTextSize(16);

        boolean enabled = preferences.getBoolean(
                EXPIRY_ALERTS_KEY, true);

        expirySwitch.setChecked(enabled);
        layout.addView(expirySwitch);

        expirySwitch.setOnCheckedChangeListener((button, isChecked) -> {
            preferences.edit()
                    .putBoolean(EXPIRY_ALERTS_KEY, isChecked)
                    .apply();

            String message = isChecked
                    ? "Expiry reminders preference enabled."
                    : "Expiry reminders preference disabled.";

            Toast.makeText(
                    SettingsActivity.this,
                    message,
                    Toast.LENGTH_SHORT
            ).show();
        });

        TextView note = new TextView(this);
        note.setText(
                "Note: This setting saves your preference. " +
                        "Automatic notifications will need to be " +
                        "implemented separately.");
        note.setTextSize(14);
        note.setPadding(0, 20, 0, 24);
        layout.addView(note);

        Button backButton = new Button(this);
        backButton.setText("Back");
        layout.addView(backButton);

        backButton.setOnClickListener(v -> finish());

        setContentView(layout);
    }
}
