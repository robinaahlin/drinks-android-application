package com.example.drinksandroidapplication.entities;

public class Ingredient {

    private String id;
    private String name;
    private String description;
    private String type;
    private String alcohol;
    private String abv;

    public Ingredient(String id, String name, String description,
                      String type, String alcohol, String abv) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.alcohol = alcohol;
        this.abv = abv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }
}
