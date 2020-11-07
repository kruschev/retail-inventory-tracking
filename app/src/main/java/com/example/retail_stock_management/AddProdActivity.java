package com.example.retail_stock_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class AddProdActivity extends AppCompatActivity {

    public static final int REQUEST_UPLOAD = 1;
    private Uri imageUri;
    private String prodCategory;
    private ArrayList<String> prodCategoryList = new ArrayList<>();
    private StorageReference ProductImageRef;
    private DatabaseReference DataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);

        ProductImageRef = FirebaseStorage.getInstance().getReference();
        DataRef = FirebaseDatabase.getInstance().getReference();

        prodCategoryList.add("Pick category");
        prodCategoryList.add("New category");

        DatabaseReference CategoryDataRef = DataRef.child("category");
        CategoryDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String value = dataSnapshot.getKey();
                prodCategoryList.add(value);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        final Spinner spinner = findViewById(R.id.prod_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prodCategoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String selectedCategory = spinner.getSelectedItem().toString();


                if (selectedCategory == "New category") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProdActivity.this);

                    View viewInflated = LayoutInflater.from(AddProdActivity.this).inflate(R.layout.popup_add_prod_category, null, false);
                    final EditText new_category_input = viewInflated.findViewById(R.id.popupNewCategory);

                    builder.setTitle("New category name:");
                    builder.setView(viewInflated);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            prodCategory  = new_category_input.getText().toString();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                else if (selectedCategory != "Pick category"){
                    prodCategory = selectedCategory;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void pick_img(View view) {
        Intent upload_intent = new Intent();
        upload_intent.setAction(Intent.ACTION_GET_CONTENT);
        upload_intent.setType("image/*");
        startActivityForResult(upload_intent, REQUEST_UPLOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPLOAD && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ImageView ImageDisplay = findViewById(R.id.upload_display);
            ImageDisplay.setImageURI(imageUri);
        }
    }

    public void upload_img(View view) {
        EditText ProductName = findViewById(R.id.prod_name);
        EditText ProductPrice = findViewById(R.id.prod_price);
        //EditText ProductCategory = findViewById(R.id.prod_cat);
        final String prodName = ProductName.getText().toString();
        final String prodPrice = ProductPrice.getText().toString();
        //final String prodCategory = ProductCategory.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Missing product image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prodName)) {
            Toast.makeText(this, "Missing product name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prodPrice)) {
            Toast.makeText(this, "Missing product price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prodCategory)) {
            Toast.makeText(this, "Missing product category", Toast.LENGTH_SHORT).show();
        }
        else {
            final StorageReference filePath = ProductImageRef.child("prodImages/" + imageUri.getLastPathSegment());
            final UploadTask uploadTask = filePath.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        final String image_url = downloadUri.toString();

                        saveToDatabase(prodName, prodPrice, prodCategory, image_url);
                    } else {
                        // Handle failures
                        Toast.makeText(AddProdActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void saveToDatabase(String prodName, String prodPrice, String prodCategory, String image_url) {
        DatabaseReference ProductDataRef = DataRef.child("product");
        String prodID = ProductDataRef.push().getKey();

        Product newProduct = new Product(prodName, prodPrice, prodCategory, image_url);

        ProductDataRef.child(prodID).setValue(newProduct);
    }
}

@IgnoreExtraProperties
class Product {
    public String productName;
    public String productPrice;
    public String prodCategory;
    public String productImage;

    public Product() {

    }

    public Product (String productName, String productPrice, String prodCategory, String productImage){
        this.productName = productName;
        this.productPrice = productPrice;
        this.prodCategory = prodCategory;
        this.productImage = productImage;
    }
}