package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import java.util.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailActivity extends AppCompatActivity {
    TextView recipeNameText;
    TextView recipeIngredientsText;
    TextView recipeStepsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setTitle("Recipe");

         recipeNameText = findViewById(R.id.recipeNameText);
         recipeIngredientsText = findViewById(R.id.recipeIngredientsText);
         recipeStepsText = findViewById(R.id.recipeStepsText);

         //get recipe id from possibleActivity
        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(recipeId);
        List<Ingredient> ingredients = db.getIngredientsNeeded(recipeId);

        //ingredient list
        StringBuilder ingredientText = new StringBuilder();
        for(Ingredient i : ingredients){
            ingredientText.append("- ").append(i.name).append(": ").append(i.quantityText()).append("\n");
        }

        recipeNameText.setText(recipe.name);
        recipeIngredientsText.setText(ingredientText.toString());
        recipeStepsText.setText(recipe.steps);
    }
}