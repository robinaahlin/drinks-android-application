package com.example.drinksandroidapplication.controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.drinksandroidapplication.MainActivity;
import com.example.drinksandroidapplication.R;
import com.example.drinksandroidapplication.adapters.CustomAdapter;
import com.example.drinksandroidapplication.entities.Drink;
import com.example.drinksandroidapplication.entities.Ingredient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ControllerAPI implements InterfaceControllerAPI {

    private static final String GET_INGREDIENT_BY_NAME =
            "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=";
    private static final String GET_DRINKS_BY_NAME =
            "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";

    private URL url;
    private HttpURLConnection urlConnection = null;
    private Context context;

    private List<Drink> drinkList;
    private CustomAdapter adapter;
    private RecyclerView listViewDrinks;
    private MainActivity mainActivity;

    public ControllerAPI(MainActivity mainActivity, Context context, RecyclerView listView) {
        this.context = context;
        this.drinkList = new ArrayList<>();
        this.listViewDrinks = listView;
        this.mainActivity = mainActivity;
    }

    @Override
    public void getDrinkByName(String name, CustomAdapter drinkAdapter) {
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        adapter = drinkAdapter;
        drinkList.clear();

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_DRINKS_BY_NAME + name,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("drinks");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONArray keys = jsonObject.names();

                                Drink drink = new Drink();
                                List<String> ingredients = new LinkedList<>();
                                List<String> measures = new LinkedList<>();

                                assert keys != null;
                                for(int k = 0; k<keys.length(); k++){
                                    String key = keys.getString(k);
                                    if ("idDrink".equals(key)) {
                                        drink.setId(jsonObject.getString(key));
                                    } else if ("strDrink".equals(key)) {
                                        drink.setName(jsonObject.getString(key));
                                    } else if ("strCategory".equals(key)) {
                                        drink.setCategory(jsonObject.getString(key));
                                    } else if ("strAlcoholic".equals(key)) {
                                        drink.setAlcoholic(jsonObject.getString(key));
                                    } else if ("strGlass".equals(key)) {
                                        drink.setGlass(jsonObject.getString(key));
                                    } else if ("strInstructions".equals(key)) {
                                        drink.setInstructions(jsonObject.getString(key));
                                    } else if (key.contains("Ingredient")) {
                                        if(!jsonObject.getString(key).equals("null")){
                                            ingredients.add(jsonObject.getString(key));
                                        }
                                    } else if(key.contains("Measure")){
                                        if(!jsonObject.getString(key).equals("null")){
                                            measures.add(jsonObject.getString(key));
                                        }
                                    } else if("strDrinkThumb".equals(key)){
                                        drink.setImageURL(jsonObject.getString(key).replace("\\", ""));
                                    }
                                }
                                drink.setIngredients(ingredients);
                                drink.setMeasures(measures);
                                drinkList.add(drink);
                            }

                            if(adapter == null){
                                adapter = new CustomAdapter(mainActivity, context, drinkList);
                                listViewDrinks.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    @Override
    public void getIngredientInfo(String name) {
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_INGREDIENT_BY_NAME + name,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest response", response.toString());

                        String k = response.keys().next();
                        try {
                            String tmp = response.getJSONArray(k).getString(0);
                            JsonObject jsonObject = new Gson().fromJson(tmp, JsonObject.class);
                            Ingredient ingredient = new Ingredient(
                                    jsonObject.get("idIngredient").toString(),
                                    jsonObject.get("strIngredient").toString(),
                                    jsonObject.get("strDescription").toString(),
                                    jsonObject.get("strType").toString(),
                                    jsonObject.get("strAlcohol").toString(),
                                    jsonObject.get("strABV").toString()
                            );
                            TextView textView = ((Activity) context).findViewById(R.id.textView);
                            textView.setText(ingredient.getName());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());
                    }
                }

        );
        requestQueue.add(objectRequest);
    }

    @Override
    public void getFavorites() {
        List<Drink> list = SingletonFavorites.getInstance().getList();
        if(adapter == null){
            adapter = new CustomAdapter(mainActivity, context, list);
            listViewDrinks.setAdapter(adapter);
        }else{
            drinkList.clear();
            drinkList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addFavorite(Drink drink) {
        SingletonFavorites.getInstance().addDrink(drink);
    }
}
