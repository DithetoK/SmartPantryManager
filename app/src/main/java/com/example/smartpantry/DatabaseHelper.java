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
    private static final int DB_VERSION = 2;

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
    }

    }

