package com.example.drinksandroidapplication.controller;

import com.example.drinksandroidapplication.entities.Drink;

import java.util.ArrayList;
import java.util.List;

public class SingletonFavorites {

    private List<Drink> list;
    private static SingletonFavorites instance = null;

    private SingletonFavorites(){
        list = new ArrayList<>();
    }

    public static SingletonFavorites getInstance(){
        if(instance == null){
            instance = new SingletonFavorites();
        }
        return instance;
    }

    public void addDrink(Drink drink){
        if(!alreadyAdded(drink))
            list.add(drink);
    }

    private boolean alreadyAdded(Drink drink){
        for(Drink d : list){
            if(d.getId().equals(drink.getId())){
                return true;
            }
        }
        return false;
    }

    public List<Drink> getList(){
        return list;
    }

    // Could be set as package private
    public void setList(List<Drink> list) {
        this.list = list;
    }
}
