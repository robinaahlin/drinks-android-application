package com.example.drinksandroidapplication.controller;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.drinksandroidapplication.R;

public class SearchDialog extends Dialog implements View.OnClickListener {

    private ControllerAPI controllerAPI;

    private EditText edtSearch;

    public SearchDialog(Activity a, ControllerAPI c) {
        super(a);
        this.controllerAPI = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);

        edtSearch = findViewById(R.id.editText_search);
        Button btnSearch = findViewById(R.id.button_search);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_search) {
            String drink = edtSearch.getText().toString();
            controllerAPI.getDrinkByName(drink, null);
        }
        dismiss();
    }
}
