package com.example.retail_stock_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SellActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
    }

    public void sell(View view) {
        Intent intent = new Intent(this, PhatActivity.class);
        startActivity(intent);
    }
}