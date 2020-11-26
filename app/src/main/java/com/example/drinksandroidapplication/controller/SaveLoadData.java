package com.example.drinksandroidapplication.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.drinksandroidapplication.entities.Drink;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadData {

    private static final String FILE_NAME = "DRINK_APPLICATION_LIST";

    private static SharedPreferences sharedPreferences;

    public SaveLoadData(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    public void set(String key, List<Drink> drink) {
        Gson gson = new Gson();
        String data = gson.toJson(drink);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public void set(String key_DrinkName, Drink drink) {
        Gson gson = new Gson();
        String data = gson.toJson(drink);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_DrinkName, data);
        editor.apply();
    }

    // Load list from sharedPreferences
    public void loadList(String key){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<Drink>>(){}.getType();
        List<Drink> drinks = gson.fromJson(json, type);
        if(drinks == null){
            drinks = new ArrayList<>();
        }
        SingletonFavorites.getInstance().setList(drinks);
    }

    // Load Single Drink from sharedPreferences
    public void load(String key_DrinkName) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key_DrinkName, "");
        Drink drink = gson.fromJson(json, Drink.class);
    }
}
