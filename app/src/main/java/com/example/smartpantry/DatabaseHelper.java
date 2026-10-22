package com.example.smartpantry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {
    private  static final String DB_NAME = "pantry.db";
    private static final int DB_VERSION = 3;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, quantity REAL, unit TEXT, expiry TEXT)");

        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT,steps TEXT)");

        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER, name TEXT, quantity REAL, unit TEXT)");

        Recipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pantry");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);
    }

        //create
        public void addItem(Ingredient item){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", item.name);
            values.put("quantity", item.quantity);
            values.put("unit", item.unit);
            values.put("expiry", item.expiry);
            db.insert("pantry", null, values);
        }

        //get all ingredients
        public List<Ingredient> getAllItems(){
        List<Ingredient> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, quantity, unit, expiry FROM pantry ORDER BY name", null);
        while(cursor.moveToNext()){
            list.add(new Ingredient(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4)));
        }
        cursor.close();
        return list;
        }

        //read one item
    public Ingredient getItem(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, quantity, unit, expiry FROM pantry WHERE id = ?",
                new String[]{String.valueOf(id)});
        Ingredient item = null;
        if(cursor.moveToFirst()){
            item = (new Ingredient(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4)));
        }

        cursor.close();
        return item;
    }

    //update
    public void updateItem(Ingredient item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.name);
        values.put("quantity", item.quantity);
        values.put("unit", item.unit);
        values.put("expiry", item.expiry);
        db.update("pantry", values, "id =?", new String[]{String.valueOf(item.id)});
    }

    //delete
    public void deleteItem(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("pantry", "id =?", new String[]{String.valueOf(id)});
    }

    //geta all recipes
    public List<Recipe> getAllRecipes(){
        List<Recipe> recipesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, steps FROM recipes ORDER BY name", null);
        while(cursor.moveToNext()){
            recipesList.add(new Recipe(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        return recipesList;
    }

    //get one recipe
    public Recipe getRecipe(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, steps FROM recipes WHERE id = ?",
                new String[]{String.valueOf(id)});
        Recipe recipe = null;
        if(cursor.moveToFirst()){
            recipe = new Recipe(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        return recipe;
    }

    //get ingredients needed for 1 recipe
    public List<Ingredient> getIngredientsNeeded(int recipeId){
        List<Ingredient> ingredientList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, quantity, unit FROM recipe_ingredients WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)});
        while(cursor.moveToNext()){
            ingredientList.add(new Ingredient(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), ""));
        }
        cursor.close();
        return ingredientList;
    }

    //get matching recipes
    public List<Recipe> getPossibleRecipes(){
        List<Ingredient> pantry = getAllItems();
        List<Recipe> possible = new ArrayList<>();
        for(Recipe recipe : getAllRecipes()){
            List<Ingredient> required = getIngredientsNeeded(recipe.id);
            if(MatchRecipes.isPossible(required,pantry)){
                possible.add(recipe);
            }
        }
        return possible;
    }


    //Add recipes and ingredients
    private void addRecipe(SQLiteDatabase db, String name, String steps, String... ingredients){
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("steps", steps);
        long recipeId = db.insert("recipes", null, values);

        for(String text : ingredients){
            String[] parts = text.split(",");
            ContentValues cv = new ContentValues();
            cv.put("recipe_id", recipeId);
            cv.put("name", parts[0]);
            cv.put("quantity",  Double.parseDouble(parts[1]));
            cv.put("unit", parts[2]);
            db.insert("recipe_ingredients",null, cv);
        }
    }

    private void Recipes(SQLiteDatabase db){
        addRecipe(db, "French Toast", "1. Whisk the egg, milk, and cinnamon in a shallow dish." +
                        "\n2. Dip bread slices into the mixture until soaked." +
                "\n3. Fry in butter until golden brown on both sides.",
                "bread,2,pcs", "egg,1,pcs", "milk,50,ml", "butter,10,g", "cinnamon,1,g");

        addRecipe(db, "Macaroni and Cheese", "1. Boil macaroni in salted water until tender, then drain." +
                        "\n2. Mix hot macaroni with cheddar cheese, milk, and butter." +
                        "\n3. Stir continuously over low heat until a creamy sauce forms.",
                "macaroni,100,g", "cheddar cheese,80,g", "milk,50,ml", "butter,15,g");

        addRecipe(db, "Hot Chocolate", "1. Heat milk in a small saucepan over medium heat." +
                "\n2. Whisk in cocoa powder and sugar until fully dissolved.\n3. Pour into a mug and serve warm.",
                "milk,250,ml", "cocoa powder,10,g", "sugar,15,g");

        addRecipe(db, "Fruit Salad", "1. Chop the apple, banana, and strawberries into bite-sized pieces." +
                        "\n2. Toss them together gently in a large bowl.\n3. Drizzle with honey and mix.",
                "apple,1,pcs", "banana,1,pcs", "strawberries,100,g", "honey,15,ml");

        addRecipe(db, "Mashed Potatoes", "1. Boil peeled and cubed potatoes until tender." +
                "\n2. Drain well, then add butter and milk.\n3. Mash until smooth and season with salt.",
                "potato,500,g", "butter,30,g", "milk,50,ml", "salt,3,g");

        addRecipe(db, "Pancakes", "1. Whisk the flour, sugar, and baking powder." +
                        "\n2. Mix in milk, egg, and melted butter.\n3. Pour batter onto a hot griddle and flip when bubbly.",
                "flour,120,g", "milk,200,ml", "egg,1,pcs", "sugar,20,g", "butter,15,g");

        addRecipe(db, "Stir-Fry Veggies", "1. Slice the bell pepper and broccoli." +
                        "\n2. Heat vegetable oil in a wok and stir-fry the vegetables for 5 minutes." +
                        "\n3. Stir in the soy sauce and cook 1 more minute.",
                "bell pepper,1,pcs", "broccoli,100,g", "vegetable oil,15,ml", "soy sauce,20,ml");

        addRecipe(db, "Chia Pudding", "1. Whisk chia seeds, milk, and maple syrup together in a jar." +
                        "\n2. Let it sit for 10 minutes, then whisk again to avoid lumps." +
                        "\n3. Refrigerate for at least 4 hours until thick.",
                "chia seeds,30,g", "milk,150,ml", "maple syrup,15,ml");

        addRecipe(db, "Boiled Eggs", "1. Place eggs in a saucepan and cover with water." +
                        "\n2. Bring to a boil, then cook for exactly 7 minutes for medium yolks." +
                        "\n3. Transfer to ice water, peel, and serve.",
                "egg,2,pcs", "water,500,ml");

        addRecipe(db, "Couscous", "1. Bring water and salt to a boil in a pot." +
                "\n2. Stir in couscous, cover, and remove from heat.\n3. Let sit for 5 minutes, then fluff with a fork.",
                "couscous,80,g", "water,120,ml", "salt,1,g");

        addRecipe(db, "Onion Rings", "1. Slice onion into rings and separate them." +
                        "\n2. Dip rings in milk, then dredge in flour.\n3. Fry in hot oil until golden and crisp.",
                "onion,1,pcs", "flour,50,g", "milk,50,ml", "oil,200,ml");

        addRecipe(db, "French Fries", "1. Cut potatoes into thin matchstick strips." +
                        "\n2. Soak in cold water, then dry thoroughly.\n3. Deep fry in hot vegetable oil until crisp and golden.",
                "potato,300,g", "vegetable oil,300,ml", "salt,3,g");

        addRecipe(db, "Buffalo Wings", "1. Bake or fry chicken wings until crispy." +
                        "\n2. Melt butter and whisk it together with hot sauce.\n3. Toss the cooked wings in the sauce until fully coated.",
                "chicken wings,300,g", "hot sauce,40,ml", "butter,20,g");

        addRecipe(db, "Egg Salad", "1. Mash hard-boiled eggs in a bowl.\n2. Mix in mayonnaise and mustard." +
                "\n3. Season with salt and pepper to taste.",
                "egg,2,pcs", "mayonnaise,25,g", "mustard,5,g", "salt,1,g");

        addRecipe(db, "Pesto Pasta", "1. Cook penne pasta in boiling salted water.\n2. Drain pasta, saving a splash of cooking water." +
                        "\n3. Toss pasta with basil pesto and a touch of cheese.",
                "penne pasta,100,g", "basil pesto,40,g", "parmesan cheese,10,g");

        addRecipe(db, "Coleslaw", "1. Finely shred the green cabbage.\n2. Whisk mayonnaise, vinegar, and sugar in a large bowl." +
                        "\n3. Toss cabbage with dressing until evenly coated.",
                "cabbage,200,g", "mayonnaise,40,g", "vinegar,10,ml", "sugar,5,g");

        addRecipe(db, "Cinnamon Apples", "1. Core and slice the apples.\n2. Melt butter in a pan and toss in apples and brown sugar." +
                        "\n3. Cook until tender, then stir in the cinnamon.",
                "apple,2,pcs", "butter,15,g", "brown sugar,15,g", "cinnamon,2,g");

        addRecipe(db, "Baked Sweet Potato", "1. Scrub sweet potato and prick multiple times with a fork.\n2. Rub skin lightly with vegetable oil." +
                        "\n3. Bake at 200C for 45 minutes until soft.",
                "sweet potato,1,pcs", "vegetable oil,5,ml");

        addRecipe(db, "Tacos", "1. Brown the ground beef in a skillet and drain fat.\n2. Stir in taco seasoning and water, simmering for 5 minutes." +
                        "\n3. Spoon beef into taco shells and top with lettuce.",
                "ground beef,150,g", "taco seasoning,15,g", "water,50,ml", "taco shells,3,pcs", "lettuce,20,g");

        addRecipe(db, "Avocado Toast", "1. Toast the bread until golden and crisp.\n2. Mash the avocado with lemon juice and salt." +
                        "\n3. Spread onto the toast and top with red pepper flakes.",
                "bread,1,pcs", "avocado,0.5,pcs", "lemon juice,5,ml", "salt,1,g", "red pepper flakes,0.5,g");
    }

    }

