package com.example.retail_stock_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity {

    private DatabaseReference DataRef;
    private ArrayList<String> prodCategoryList = new ArrayList<>();

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

                RecyclerView recyclerView = findViewById(R.id.recycler_view_category);
                int numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(SellActivity.this, numberOfColumns));

                RecyclerButtonViewAdapter adapter = new RecyclerButtonViewAdapter(prodCategoryList, new RecyclerButtonViewAdapter.OnItemClickListener() {
                    @Override public void onItemClick(String button_text) {
                        Toast.makeText(SellActivity.this, "Item Clicked" + button_text, Toast.LENGTH_LONG).show();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}