package com.example.retail_stock_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity {

    private DatabaseReference DataRef;
    private ArrayList<String> prodCategoryList = new ArrayList<>();
    public static final int NUMBER_OF_COLUMNS = 2;
    public static final String CATEGORY_NAME = "com.example.retail_stock_management.CATEGORY_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        DataRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference CategoryDataRef = DataRef.child("category");

        CategoryDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot category: dataSnapshot.getChildren()) {
                    String category_name = category.getValue(String.class);
                    prodCategoryList.add(category_name);
                }

                RecyclerView recyclerView = findViewById(R.id.recycler_view_category);
                recyclerView.setLayoutManager(new GridLayoutManager(SellActivity.this, NUMBER_OF_COLUMNS));

                RecyclerButtonViewAdapter Adapter = new RecyclerButtonViewAdapter(prodCategoryList, new RecyclerButtonViewAdapter.OnItemClickListener() {
                    @Override public void onItemClick(String button_text) {
                        Intent ProductSell = new Intent(SellActivity.this, ProdSellActivity.class);
                        ProductSell.putExtra(CATEGORY_NAME, button_text);
                        startActivity(ProductSell);
                    }
                });
                recyclerView.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}