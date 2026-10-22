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
    private static final int DB_VERSION = 1;

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
    }

