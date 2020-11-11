package com.example.retail_stock_management;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Comparator;

@IgnoreExtraProperties
class Product {
    public String id;
    public String name;
    public String priceSell;
    public String quant;
    public String category;
    public String imageUrl;

    public String priceBuyCurrentBatch;
    public String priceBuyNextBatches;
    public String quantCurrentBatch;
    public String quantNextBatches;

    public Product() {

    }

    public Product (String prodID, String prodName, String prodPriceSell, String prodQuant, String prodCategory, String prodImageUrl,
                    String prodPriceBuyCurrent, String prodPriceBuyNext, String prodQuantCurrent, String prodQuantNext){
        this.id = prodID;
        this.name = prodName;
        this.priceSell = prodPriceSell;
        this.quant = prodQuant;
        this.category = prodCategory;
        this.imageUrl = prodImageUrl;

        this.priceBuyCurrentBatch = prodPriceBuyCurrent;
        this.priceBuyNextBatches = prodPriceBuyNext;
        this.quantCurrentBatch = prodQuantCurrent;
        this.quantNextBatches = prodQuantNext;
    }
}

class ProductCompare implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        // write comparison logic here like below , it's just a sample
        return p1.name.compareTo(p2.name);
    }
}