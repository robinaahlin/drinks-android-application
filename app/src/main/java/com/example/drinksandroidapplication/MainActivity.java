package com.example.drinksandroidapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.drinksandroidapplication.controller.ControllerAPI;
import com.example.drinksandroidapplication.controller.SaveLoadData;
import com.example.drinksandroidapplication.controller.SearchDialog;
import com.example.drinksandroidapplication.controller.SingletonFavorites;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button buttonSearch, buttonFavorites, buttonSave;
    //private ListView listViewDrinks;
    private RecyclerView listViewDrinks;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        buttonSearch = findViewById(R.id.button_search);
        buttonFavorites = findViewById(R.id.button_favorites);
        buttonSave = findViewById(R.id.button_save);
        //listViewDrinks = findViewById(R.id.listViewProduct);

        listViewDrinks = findViewById(R.id.recyclerView_listViewProduct);
        listViewDrinks.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listViewDrinks.setLayoutManager(layoutManager);
        listViewDrinks.setItemAnimator(new DefaultItemAnimator());

        final ControllerAPI controllerAPI = new ControllerAPI(this, this, listViewDrinks);

        final SaveLoadData saveLoadData = new SaveLoadData(getApplicationContext());
        saveLoadData.loadList("Drinks_List");

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog searchDialog = new SearchDialog(MainActivity.this, controllerAPI);
                searchDialog.show();
            }
        });

        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerAPI.getFavorites();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoadData.set("Drinks_List", SingletonFavorites.getInstance().getList());
            }
        });
    }
}
