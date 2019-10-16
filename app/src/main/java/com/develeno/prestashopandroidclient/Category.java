package com.develeno.prestashopandroidclient;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;

public class Category {
    private String URL;
    private ArrayList<Category> childCategories = new ArrayList<>();
    private int depth;

    /* renamed from: id */
    private int f22id;
    /* access modifiers changed from: private */
    public Bitmap image;
    private int imageId;
    private String label = "Unnamed Category";
    private Category parentCategory;
    private String parentCategoryURL;

    public int getImageId() {
        return this.imageId;
    }

    public void setImageId(int imageId2) {
        this.imageId = imageId2;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL2) {
        this.URL = URL2;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth2) {
        this.depth = depth2;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }

    public ArrayList<Category> getChildCategories() {
        return this.childCategories;
    }

    public void setChildCategories(ArrayList<Category> childCategories2) {
        this.childCategories = childCategories2;
    }

    public Category getParentCategory() {
        return this.parentCategory;
    }

    public void setParentCategory(Category parentCategory2) {
        this.parentCategory = parentCategory2;
    }

    public String getParentCategoryURL() {
        return this.parentCategoryURL;
    }

    public void setParentCategoryURL(String parentCategoryURL2) {
        this.parentCategoryURL = parentCategoryURL2;
    }

    public boolean isParentOf(Category parentCategory2) {
        return parentCategory2.getURL().contentEquals(this.parentCategoryURL);
    }

    public void addToChildCategories(Category category) {
        this.childCategories.add(category);
    }

    public int getId() {
        return this.f22id;
    }

    public void setId(int id) {
        this.f22id = id;
        this.URL = "http://www.vegetableshoppy.com/api/categories/" + id;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> filteredproducts = new ArrayList<>();
        Iterator it = Data.getProducts().iterator();
        while (it.hasNext()) {
            Product product = (Product) it.next();
            if (!(product == null || product.getCategoryId() == null || product.getCategoryId().intValue() != getId())) {
                filteredproducts.add(product);
            }
        }
        return filteredproducts;
    }

    public void getImage(OnBackgroundTaskCompleted taskCompleted) {
        if (this.image == null) {
            getImageInBackground(taskCompleted);
        } else {
            taskCompleted.getResult(this.image);
        }
    }

    private void getImageInBackground(final OnBackgroundTaskCompleted taskCompleted) {
        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");
        query.whereEqualTo("category_id", Integer.valueOf(this.f22id));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.getParseFile("pic").getDataInBackground((GetDataCallback) new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                taskCompleted.getResult(bitmap);
                                Category.this.image = bitmap;
                                return;
                            }
                            taskCompleted.getResult(null);
                        }
                    });
                } else {
                    taskCompleted.getResult(null);
                }
            }
        });*/
    }

    public void setImage(Bitmap image2) {
        this.image = image2;
    }
}
