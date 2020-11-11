package com.example.retail_stock_management;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Comparator;

@IgnoreExtraProperties
class Product {
    public String productName;
    public String productPriceSell;
    public String productQuant;
    public String prodCategory;
    public String productImageUrl;

    public String productPriceBuyCurrentBatch;
    public ArrayList<String> productPriceBuyNextBatches;
    public String productQuantCurrentBatch;
    public ArrayList<String> productQuantNextBatches;

    public Product() {

    }

    public Product (String prodName, String prodPriceSell, String prodQuant, String prodCategory, String prodImageUrl,
                    String prodPriceBuyCurrent, ArrayList<String> prodPriceBuyNext, String prodQuantCurrent, ArrayList<String> prodQuantNext){
        this.productName = prodName;
        this.productPriceSell = prodPriceSell;
        this.productQuant = prodQuant;
        this.prodCategory = prodCategory;
        this.productImageUrl = prodImageUrl;

        this.productPriceBuyCurrentBatch = prodPriceBuyCurrent;
        this.productPriceBuyNextBatches = prodPriceBuyNext;
        this.productQuantCurrentBatch = prodQuantCurrent;
        this.productQuantNextBatches = prodQuantNext;
    }

    public String getName() {
        return this.productName;
    }

    public String getPrice() {
        return this.productPriceSell;
    }

    public String getQuant() {
        return this.productQuant;
    }

    public String getImageUrl() {
        return this.productImageUrl;
    }
}

class ProductCompare implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        // write comparison logic here like below , it's just a sample
        return p1.getName().compareTo(p2.getName());
    }
}