package com.ecalm.e_calm.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecalm.e_calm.model.Food;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "secret.db";

    //constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL("Create table food(id INTEGER PRIMARY KEY, name TEXT, calorie REAL, weight REAL)");

        //add data
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('cooked rice', 129, 100)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('fried egg', 210, 100)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('tempe', 34, 100)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('white bread', 270, 100)");
        db.execSQL("INSERT INTO food(name,calorie,weight) VALUES('fried chicked', 400, 100)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS food");
        // create new tables
        onCreate(db);
    }

    public boolean insertFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", food.getName());
        values.put("calorie", food.getCalorie());
        values.put("weight", food.getWeight());
        // insert row
        long ins = db.insert("food", null, values);
        // assigning tags to todo
        if (ins == -1 ){
            return false;
        }else {
            return true;
        }
    }


    //fetch all data todo
    public List<Food> getAllToDos() {
        List<Food> foods = new ArrayList<Food>();
        String selectQuery = "SELECT  * FROM 'food'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Food td = new Food();
                td.setId(c.getInt((c.getColumnIndex("id"))));
                td.setName((c.getString(c.getColumnIndex("name"))));
                td.setCalorie((c.getFloat(c.getColumnIndex("calorie"))));
                td.setWeight((c.getFloat(c.getColumnIndex("name"))));
                // adding to todo list
                foods.add(td);
            } while (c.moveToNext());
        }
        return foods;
    }

    public Food searchFood(String name){
        String selectQuery = "SELECT * FROM food WHERE name='"+name+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Food data = new Food();
        c.moveToFirst();
        data.setId(c.getInt((c.getColumnIndex("id"))));
        data.setName((c.getString(c.getColumnIndex("name"))));
        data.setCalorie((c.getFloat(c.getColumnIndex("calorie"))));
        data.setWeight((c.getFloat(c.getColumnIndex("weight"))));
        return data;
    }

    public boolean cekFood(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM food";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }
}