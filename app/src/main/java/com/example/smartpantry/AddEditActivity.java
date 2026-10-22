package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import android.view.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
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
    int itemId = -1; //add new item

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
        saveButton.setOnClickListener(v -> saveItem());
        deleteButton.setOnClickListener(v -> confirmDelete());

        itemId = getIntent().getIntExtra("item_id", -1);
        if(itemId != -1){
            setTitle("Edit Ingredient");
            Ingredient item = db.getItem(itemId);
            editName.setText(item.name);
            editQuantity.setText(String.valueOf(item.quantity));
            editExpiry.setText(item.expiry);

            String[] units = getResources().getStringArray(R.array.units);
            for(int i = 0; i < units.length; i++){
                if(units[i].equals(item.unit)){
                    spinner.setSelection(i);
                }
            }
        }
        else{
            setTitle("Add Ingredient");
            deleteButton.setVisibility(View.GONE);
        }
    }

    private  void saveItem(){
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();
        String unit = spinner.getSelectedItem().toString();

        //input validation
        if(name.isEmpty()){
            editName.setError("Please enter the name of the ingredient");
            return;
        }
        if(quantityText.isEmpty()){
            editQuantity.setError("Please eneter the quantity of the ingredient");
            return;
        }

        double quantity;

        try{
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Quantity must be a number");
            return;
        }

        if(quantity <= 0){
            editQuantity.setError("Ingredient quantity must be more more than 0");
            return;
        }

        if(!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")){
            editExpiry.setError("Expiry date must be in the format yyyy-MM-dd");
            return;
        }

        //save
        Ingredient item = new Ingredient(itemId, name, quantity, unit, expiry);
        if(itemId == -1) {
            db.addItem(item);
            Toast.makeText(this, "Ingredient is now added to pantry", Toast.LENGTH_SHORT).show();
        }
        else{
            db.updateItem(item);
            Toast.makeText(this, "Ingredient is updated", Toast.LENGTH_SHORT).show();
        }

        finish();

    }

    private void confirmDelete(){
        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure that you want to delete this ingredient?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteItem(itemId);
                    Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}