package com.example.retail_stock_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sell(View view) {
        Intent sell_intent = new Intent(this, SellActivity.class);
        startActivity(sell_intent);
    }

    public void admin(View view) {
        Intent admin_intent = new Intent(this, AddProdActivity.class);
        startActivity(admin_intent);
    }
}