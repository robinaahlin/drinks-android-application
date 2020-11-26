package com.example.drinksandroidapplication.controller;

import com.example.drinksandroidapplication.adapters.CustomAdapter;
import com.example.drinksandroidapplication.entities.Drink;

public interface InterfaceControllerAPI {
    // Returns all drinks that contains the string
    void getDrinkByName(String name, CustomAdapter drinkAdapter);
    // Return information about the searched ingredient
    void getIngredientInfo(String name);
    // Return all favorite drinks
    void getFavorites();
    // Add drink to favorite
    void addFavorite(Drink drink);
}
