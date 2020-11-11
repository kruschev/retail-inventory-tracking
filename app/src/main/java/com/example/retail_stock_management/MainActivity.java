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
        Intent Sell = new Intent(this, SellActivity.class);
        startActivity(Sell);
    }

    public void admin(View view) {
        Intent Admin = new Intent(this, AddProdActivity.class);
        startActivity(Admin);
    }
}