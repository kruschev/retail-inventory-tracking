package com.example.retail_stock_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity {

    private DatabaseReference DataRef;
    private ArrayList<String> prodCategoryList = new ArrayList<>();

    //private RecyclerButtonViewAdapter adapter;

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
                    String categoryName = category.getValue(String.class);
                    prodCategoryList.add(categoryName);
                }
                Log.d("p", prodCategoryList.toString());

                RecyclerView recyclerView = findViewById(R.id.recycler_view_category);
                int numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(SellActivity.this, numberOfColumns));
                RecyclerButtonViewAdapter adapter = new RecyclerButtonViewAdapter(prodCategoryList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });






    }
}