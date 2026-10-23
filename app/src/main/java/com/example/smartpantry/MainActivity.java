package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import  java.util.List;
import android.view.*;
import android.content.Intent;

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

        //open Add/Edit screen
        addIngredientsButton.setOnClickListener(e ->{
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

        //open Add/Edit screen
        pantryList.setOnItemClickListener((parent, view, position, id) ->{
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra("item_id", items.get(position).id);
            startActivity(intent);
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_possible){
            startActivity(new Intent(this, PossibleActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent (this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}