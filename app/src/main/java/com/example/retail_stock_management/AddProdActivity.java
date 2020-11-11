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
import java.util.Comparator;


public class AddProdActivity extends AppCompatActivity {

    public static final int REQUEST_UPLOAD = 1;
    private Uri imageUri;
    private String prodCategory;
    private String newProdCategory;
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
                String value = dataSnapshot.getValue(String.class);
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
        ArrayAdapter<String> Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prodCategoryList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String selected_category = spinner.getSelectedItem().toString();


                if (selected_category == "New category") {
                    new_category();
                }
                else if (selected_category != "Pick category"){
                    prodCategory = selected_category;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void new_category() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProdActivity.this);

        View viewInflated = LayoutInflater.from(AddProdActivity.this).inflate(R.layout.popup_add_prod_category, null, false);
        final EditText NewCategoryInput = viewInflated.findViewById(R.id.popupNewCategory);

        builder.setTitle("New category name:");
        builder.setView(viewInflated);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newProdCategory = NewCategoryInput.getText().toString();
                prodCategory  = newProdCategory;
                Toast.makeText(AddProdActivity.this, newProdCategory + " will be added to category.", Toast.LENGTH_SHORT).show();
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

    public void pick_img(View view) {
        Intent Upload = new Intent();
        Upload.setAction(Intent.ACTION_GET_CONTENT);
        Upload.setType("image/*");
        startActivityForResult(Upload, REQUEST_UPLOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPLOAD && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ImageView imageDisplay = findViewById(R.id.upload_display);
            imageDisplay.setImageURI(imageUri);
        }
    }

    public void add_product(View view) {
        EditText prodNameInput = findViewById(R.id.prod_name);
        EditText prodPriceInput = findViewById(R.id.prod_price);
        final String prodName = prodNameInput.getText().toString();
        final String prodPrice = prodPriceInput.getText().toString();

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
                        final String prodImageUrl = downloadUri.toString();

                        save_to_database(prodName, prodPrice, prodCategory, prodImageUrl);
                    } else {
                        // Handle failures
                        Toast.makeText(AddProdActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void save_to_database(String prodName, String prodPrice, String prodCategory, String prodImageUrl) {
        DatabaseReference ProductDataRef = DataRef.child("product");
        String prodID = ProductDataRef.push().getKey();

        Product NewProduct = new Product(prodID, prodName, prodPrice, "0", prodCategory, prodImageUrl, "", "", "", "");

        ProductDataRef.child(prodID).setValue(NewProduct);

        if (newProdCategory == prodCategory){
            DatabaseReference NewCategoryDataRef = DataRef.child("category");
            NewCategoryDataRef.push().setValue(prodCategory);
            newProdCategory = "";
        }
    }
}