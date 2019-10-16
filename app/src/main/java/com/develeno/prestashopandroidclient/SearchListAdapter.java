package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public AppCompatActivity activity;
    /* access modifiers changed from: private */
    public ArrayList<Product> productArrayList;

    public SearchListAdapter(ArrayList<Product> products, AppCompatActivity activity2) {
        this.activity = activity2;
        this.productArrayList = products;
    }

    public int getCount() {
        return this.productArrayList.size();
    }

    public Object getItem(int i) {
        return this.productArrayList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        RelativeLayout layout = (RelativeLayout) ((LayoutInflater) this.activity.getSystemService("layout_inflater")).inflate(R.layout.search_list_item, viewGroup, false);
        TextView price = (TextView) layout.findViewById(R.id.price);
        TextView quantity = (TextView) layout.findViewById(R.id.quantity);
        final ImageView image = (ImageView) layout.findViewById(R.id.icon);
        final Product product = (Product) this.productArrayList.get(i);
        ((TextView) layout.findViewById(R.id.name)).setText(product.getProductName());
        price.setText("Rs." + product.getPrice());
        if (product.getUnit() != null) {
            quantity.setText(" - " + product.getMinQuantity() + " " + product.getUnit());
        } else {
            quantity.setVisibility(4);
        }
        Bitmap imageBitmap = product.getImageBitmap();
        if (imageBitmap != null) {
            image.setImageBitmap(imageBitmap);
        } else {
            NetworkConnection.fetchImageInBackground(product.getImageLink(), new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    Bitmap bitmap = (Bitmap) result;
                    image.setImageBitmap(bitmap);
                    product.setImageBitmap(bitmap);
                    Product productByURL = Data.getProductByURL(product.getURL());
                    if (productByURL != null) {
                        productByURL.setImageBitmap(bitmap);
                    }
                }

                public void refresh(Object result) {
                }
            }, false);
        }
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductPageActivity.setProductArrayList(SearchListAdapter.this.productArrayList, i);
                SearchListAdapter.this.activity.startActivity(new Intent(SearchListAdapter.this.activity, ProductPageActivity.class));
            }
        });
        return layout;
    }
}
