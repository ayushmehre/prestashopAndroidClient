package com.develeno.prestashopandroidclient;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
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

public class CartListAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public AppCompatActivity activity;
    /* access modifiers changed from: private */
    public ArrayList<CartItem> items;

    public CartListAdapter(AppCompatActivity activity2, ArrayList<CartItem> items2) {
        this.activity = activity2;
        this.items = items2;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int i) {
        return this.items.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.cart_item, viewGroup, false);
        final CartItem item = this.items.get(i);
        TextView name = layout.findViewById(R.id.product_name);
        ImageView image = layout.findViewById(R.id.image);
        TextView price = layout.findViewById(R.id.price);
        TextView sale = layout.findViewById(R.id.sale);
        ((TextView) layout.findViewById(R.id.quantity)).setText(item.getQuantity() + "");
        final Product product = item.getItem();
        name.setText(product.getProductName());
        image.setImageBitmap(product.getImageBitmap());
        price.setText("Rs." + (product.getPrice() * ((double) product.getMinQuantity())) + " each " + product.getMinQuantity() + " " + product.getUnit());
        if (product.isOnSale()) {
            sale.setVisibility(View.VISIBLE);
        } else {
            sale.setVisibility(View.GONE);
        }
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CartListAdapter.this.setUpDialog(inflater, item, product);
            }
        });
        return layout;
    }

    /* access modifiers changed from: private */
    public void setUpDialog(LayoutInflater inflater, CartItem item, Product product) {
        Builder builder = new Builder(this.activity);
        View layout = inflater.inflate(R.layout.cart_product_enlarge, null);
        TextView name = layout.findViewById(R.id.pr_name);
        final ImageView image = layout.findViewById(R.id.image);
        TextView price = layout.findViewById(R.id.price);
        TextView unit = layout.findViewById(R.id.quantity);
        TextView textView = layout.findViewById(R.id.sale);
        TextView done = layout.findViewById(R.id.done);
        final TextView count = layout.findViewById(R.id.count);
        ImageView add = layout.findViewById(R.id.add_button);
        ImageView remove = layout.findViewById(R.id.remove_button);
        final Product productToView = item.getItem();
        Bitmap imageBitmap = productToView.getImageBitmap();
        if (imageBitmap != null) {
            image.setImageBitmap(imageBitmap);
        } else {
            String imageLink = productToView.getImageLink();
            OnBackgroundTaskCompleted r0 = new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    Bitmap bitmap = (Bitmap) result;
                    productToView.setImageBitmap(bitmap);
                    image.setImageBitmap(bitmap);
                    Product productById = Data.getProductByURL(productToView.getURL());
                    if (productById != null) {
                        productById.setImageBitmap(bitmap);
                    }
                }

                public void refresh(Object result) {
                }
            };
            NetworkConnection.fetchImageInBackground(imageLink, r0, false);
        }
        name.setText(productToView.getProductName());
        price.setText("Rs." + productToView.getPrice());
        if (productToView.getUnit() != null) {
            unit.setText(" - " + productToView.getMinQuantity() + " " + productToView.getUnit());
        } else {
            unit.setVisibility(View.GONE);
        }
        final int[] countInt = {item.getQuantity()};
        count.setText(countInt[0] + "");
        final Product product2 = product;
        OnClickListener r02 = new OnClickListener() {
            public void onClick(View view) {
                if (countInt[0] < 10) {
                    int[] iArr = countInt;
                    iArr[0] = iArr[0] + 1;
                    count.setText(countInt[0] + "");
                    Cart.addItems(new CartItem(product2, 1), CartListAdapter.this.activity);
                }
            }
        };
        add.setOnClickListener(r02);
        final Product product3 = product;
        OnClickListener r03 = new OnClickListener() {
            public void onClick(View view) {
                if (countInt[0] > 0) {
                    int[] iArr = countInt;
                    iArr[0] = iArr[0] - 1;
                    count.setText(countInt[0] + "");
                    Cart.removeItems(product3, 1);
                }
            }
        };
        remove.setOnClickListener(r03);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();
        OnClickListener r04 = new OnClickListener() {
            public void onClick(View view) {
                CartActivity cartActivity = (CartActivity) CartListAdapter.this.activity;
                cartActivity.setupActionBar();
                cartActivity.calcTotal();
                CartListAdapter.this.notifyDataSetChanged();
                if (CartListAdapter.this.items.size() == 0) {
                    cartActivity.makeEmptyLayoutVisible();
                }
                dialog.dismiss();
            }
        };
        done.setOnClickListener(r04);
    }
}
