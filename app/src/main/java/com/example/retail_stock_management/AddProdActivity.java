package com.example.retail_stock_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class AddProdActivity extends AppCompatActivity {

    public static final int REQUEST_UPLOAD = 1;
    private Uri imageUri;
    //private ImageView ImageDisplay;
    private StorageReference ProductImageRef;
    private DatabaseReference DataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);

        ProductImageRef = FirebaseStorage.getInstance().getReference();
        DataRef = FirebaseDatabase.getInstance().getReference();
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
            ImageView ImageDisplay = (ImageView) findViewById(R.id.upload_display);
            ImageDisplay.setImageURI(imageUri);
        }
    }

    public void upload_img(View view) {
        EditText ProductName = (EditText) findViewById(R.id.prod_name);
        EditText ProductPrice = (EditText) findViewById(R.id.prod_price);
        EditText ProductBrand = (EditText) findViewById(R.id.prod_brand);
        final String prodName = ProductName.getText().toString();
        final String prodPrice = ProductPrice.getText().toString();
        final String prodBrand = ProductBrand.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Missing product image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prodName)) {
            Toast.makeText(this, "Missing product name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prodPrice)) {
            Toast.makeText(this, "Missing product price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prodBrand)) {
            Toast.makeText(this, "Missing product brand", Toast.LENGTH_SHORT).show();
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

                        saveToDatabase(prodName, prodPrice, prodBrand, image_url);
                    } else {
                        // Handle failures
                        Toast.makeText(AddProdActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void saveToDatabase(String prodName, String prodPrice, String prodBrand, String image_url) {
        DatabaseReference ProductDataRef = DataRef.child("product");
        String prodID = ProductDataRef.push().getKey();

        Product newProduct = new Product(prodName, prodPrice, prodBrand, image_url);

        ProductDataRef.child(prodID).setValue(newProduct);
    }
}

@IgnoreExtraProperties
class Product {
    public String productName;
    public String productPrice;
    public String productBrand;
    public String productImage;

    public Product() {

    }

    public Product (String productName, String productPrice, String productBrand, String productImage){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productBrand = productBrand;
        this.productImage = productImage;
    }
}