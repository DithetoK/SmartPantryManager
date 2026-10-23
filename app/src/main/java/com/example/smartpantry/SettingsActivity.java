package com.example.smartpantry;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.*;
import android.content.*;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        CheckBox checkExpiry = findViewById(R.id.checkExpiry);
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        //saved choice
        checkExpiry.setChecked(preferences.getBoolean("expiry_alerts", true));

        //save choice if box become ticked/unticked
        checkExpiry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("expiry_alerts", isChecked).apply();
                });
    }
}