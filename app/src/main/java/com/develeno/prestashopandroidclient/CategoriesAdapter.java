package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class CategoriesAdapter extends BaseAdapter {
    private final boolean asList;
    private ArrayList<Category> categories;

    {
        //filterOutParentCategories();
    }

    /* access modifiers changed from: private */
    public Context context;

    public CategoriesAdapter(Context context2, ArrayList<Category> categories2, boolean asList2) {
        this.context = context2;
        this.categories = categories2;
        this.asList = asList2;
        arrangeCategoriesByID();
    }

    private void arrangeCategoriesByID() {
        Collections.sort(this.categories, new Comparator<Category>() {
            public int compare(Category category1, Category category2) {
                return category1.getId() < category2.getId() ? -1 : 1;
            }
        });
    }

    private ArrayList<Category> filterOutParentCategories() {
        ArrayList<Category> filteredCategories = new ArrayList<>();
        for (Category category : this.categories) {
            if (category.getParentCategory() == null) {
                filteredCategories.add(category);
            }
        }
        return filteredCategories;
    }

    public int getCount() {
        return this.categories.size();
    }

    public Object getItem(int i) {
        return this.categories.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Category category = this.categories.get(i);
        if (this.asList) {
            v = inflater.inflate(R.layout.category_item, viewGroup, false);
        } else {
            v = inflater.inflate(R.layout.category_item_grid, viewGroup, false);
        }
        ((TextView) v.findViewById(R.id.text)).setText(category.getLabel());
        TextView subtext = v.findViewById(R.id.subtext);
        final ImageView icon = v.findViewById(R.id.backgound);
        int sizeOfChildCategories = category.getChildCategories().size();
        int size = category.getProducts().size();
        subtext.setText(size + " products");
        if (size > 1) {
            subtext.setText(size + " products");
        } else if (size == 0) {
            subtext.setText("Empty Category");
        } else if (size == 1) {
            subtext.setText(size + " product");
        }
        if (sizeOfChildCategories == 0) {
            openProductsActivity(v, category);
        } else {
            openProductsActivity(v, category);
        }
        /*category.getImage(new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    icon.setImageBitmap((Bitmap) result);
                }
            }

            public void refresh(Object result) {
            }
        });*/

        category.getImageInBackground(new OnBackgroundTaskCompleted() {
            @Override
            public void getResult(Object obj) {
                String img_link = category.getImg_link();
                Picasso.with(context).load(img_link).into(icon);
            }

            @Override
            public void refresh(Object obj) {

            }
        });

        return v;
    }

    /*public void getImageInBackground(ParseFile file, final OnBackgroundTaskCompleted task) {
        file.getDataInBackground((GetDataCallback) new GetDataCallback() {
            public void done(byte[] bytes, ParseException e) {
                Bitmap bitmap = null;
                if (e == null) {
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
                task.getResult(bitmap);
            }
        });
    }*/

    private void expandCategory(ViewGroup viewGroup, LayoutInflater inflater, RelativeLayout layout, Category category, ImageView arrow, LinearLayout v) {
        final Category category2 = category;
        final ImageView imageView = arrow;
        final LayoutInflater layoutInflater = inflater;
        final ViewGroup viewGroup2 = viewGroup;
        final LinearLayout linearLayout = v;
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ArrayList<Category> subCategories = category2.getChildCategories();
                imageView.setRotation(90.0f);
                Iterator it = subCategories.iterator();
                while (it.hasNext()) {
                    RelativeLayout subcategorylayout = (RelativeLayout) layoutInflater.inflate(R.layout.subcategory, viewGroup2, false);
                    ((TextView) subcategorylayout.findViewById(R.id.text1)).setText(((Category) it.next()).getLabel());
                    linearLayout.addView(subcategorylayout);
                }
            }
        });
    }

    private void openProductsActivity(View layout, final Category category) {
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsActivity.setCategoryToView(category);
                Intent intent = new Intent(CategoriesAdapter.this.context, ProductsActivity.class);
                intent.putExtra("activityName", category.getLabel());
                CategoriesAdapter.this.context.startActivity(intent);
            }
        });
    }
}
