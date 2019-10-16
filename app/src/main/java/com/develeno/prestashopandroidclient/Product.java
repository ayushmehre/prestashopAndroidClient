package com.develeno.prestashopandroidclient;

import android.graphics.Bitmap;

public class Product {
    private String URL;
    private Integer categoryId;
    private String description = "No Description Added";

    /* renamed from: id */
    private Integer f24id;
    private Bitmap imageBitmap;
    private String imageLink;
    private boolean isOnSale = false;
    private int minQuantity = 1;
    private String price = "Price not Avail";
    private String productName = "Unnamed Product";
    private String unit = "Unit";

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName2) {
        this.productName = productName2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        String description3 = description2.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "");
        if (!description3.isEmpty()) {
            this.description = description3;
        }
    }

    public double getPrice() {
        try {
            return Double.parseDouble(this.price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getUnit() {
        if (this.unit == null) {
            return "unit";
        }
        return this.unit;
    }

    public void setUnit(String unit2) {
        String unit3 = unit2.replaceAll("\n", "").replaceAll("\t", "");
        if (unit3.isEmpty()) {
            unit3 = null;
        }
        this.unit = unit3;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink2) {
        this.imageLink = imageLink2;
    }

    public boolean isOnSale() {
        return this.isOnSale;
    }

    public void setIsOnSale(boolean isOnSale2) {
        this.isOnSale = isOnSale2;
    }

    public int getMinQuantity() {
        return this.minQuantity;
    }

    public void setMinQuantity(int minQuantity2) {
        this.minQuantity = minQuantity2;
    }

    public Bitmap getImageBitmap() {
        return this.imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap2) {
        this.imageBitmap = imageBitmap2;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL2) {
        this.URL = URL2;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId2) {
        this.categoryId = categoryId2;
    }

    public int getId() {
        return this.f24id.intValue();
    }

    public void setId(int id) {
        this.f24id = Integer.valueOf(id);
        this.URL = "http://www.vegetableshoppy.com/api/products/" + id;
    }

    public boolean isAvailInStocks() {
        StockInfo stockInfo = Data.getStockInfoByProductId(this.f24id.intValue());
        if ((stockInfo != null ? stockInfo.getQuantity() : 0) > 0) {
            return true;
        }
        return false;
    }
}
