package com.example.smartpantry;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.content.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PossibleActivity extends AppCompatActivity {

    ListView possibleList;
    TextView noMatchText;
    DatabaseHelper db;
    List<Recipe> possible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible);
        setTitle("Possible Recipes");

        possibleList = findViewById(R.id.possibleList);
        noMatchText = findViewById(R.id.noMatchText);
        db = new DatabaseHelper(this);

        //tap recipe, open details screen,send recipe id
        possibleList.setOnItemClickListener((parent, view, position, id) ->{
            Intent intent = new Intent(PossibleActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", possible.get(position).id);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        possible = db.getPossibleRecipes();


        possibleList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,possible));

        if(possible.isEmpty()){
            noMatchText.setVisibility(View.VISIBLE);
        } else{
            noMatchText.setVisibility(View.GONE);
        }
    }
}