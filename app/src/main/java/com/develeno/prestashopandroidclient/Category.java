package com.develeno.prestashopandroidclient;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private String img_link;

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
        try {
            return parentCategory2.getURL().contentEquals(this.parentCategoryURL);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void addToChildCategories(Category category) {
        this.childCategories.add(category);
    }

    public int getId() {
        return this.f22id;
    }

    public void setId(int id) {
        this.f22id = id;
        this.URL = "http://www.organicdolchi.com/api/categories/" + id;
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

    public void getImageInBackground(final OnBackgroundTaskCompleted taskCompleted) {
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

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("categories")
                .document(label.toLowerCase()).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String img_link = documentSnapshot.getString("img_link");
                        Category.this.img_link = img_link;
                        taskCompleted.getResult(null);
                    }
                });
    }

    public void setImage(Bitmap image2) {
        this.image = image2;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
