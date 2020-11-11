package com.example.retail_stock_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProdSellActivity extends AppCompatActivity {

    private DatabaseReference DataRef;
    private ArrayList<Product> productList = new ArrayList<>();
    public static final int NUMBER_OF_COLUMNS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_sell);

        Intent ProductSell = getIntent();
        String category_name = ProductSell.getStringExtra(SellActivity.CATEGORY_NAME);

        DataRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProductDataRef = DataRef.child("product");

        ProductDataRef.orderByChild("category").equalTo(category_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot product_child: dataSnapshot.getChildren()) {
                    Product product = product_child.getValue(Product.class);
                    productList.add(product);
                }

                Collections.sort(productList,new ProductCompare());

                RecyclerView recyclerView = findViewById(R.id.recycler_view_productSell);
                recyclerView.setLayoutManager(new GridLayoutManager(ProdSellActivity.this, NUMBER_OF_COLUMNS));

                RecyclerItemViewAdapter Adapter = new RecyclerItemViewAdapter(productList, new RecyclerItemViewAdapter.OnItemClickListener() {
                    @Override public void onItemClick(String product_id) {

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