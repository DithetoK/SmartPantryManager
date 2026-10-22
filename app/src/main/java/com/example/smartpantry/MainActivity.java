package com.example.smartpantry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import  java.util.List;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ListView pantryList;
    TextView textEmpty;
    Button addIngredientsButton;
    DatabaseHelper db;
    List<Ingredient> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryList = findViewById(R.id.pantryList);
        textEmpty = findViewById(R.id.textEmpty);
        addIngredientsButton = findViewById(R.id.addIngredientButton);
        db = new DatabaseHelper(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        items = db.getAllItems();
        pantryList.setAdapter(new PantryAdapter(this, items));

        if(items.isEmpty()){
            textEmpty.setVisibility(View.VISIBLE);
        } else{
            textEmpty.setVisibility(View.GONE);
        }
    }
}