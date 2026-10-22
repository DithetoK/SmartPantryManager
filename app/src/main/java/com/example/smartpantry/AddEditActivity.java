package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditActivity extends AppCompatActivity {

    EditText editName;
    EditText editQuantity;
    EditText editExpiry;
    Button saveButton;
    Button deleteButton;
    DatabaseHelper db;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinner = findViewById(R.id.spinner);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        db = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,R.array.units,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((unitAdapter));
    }
}