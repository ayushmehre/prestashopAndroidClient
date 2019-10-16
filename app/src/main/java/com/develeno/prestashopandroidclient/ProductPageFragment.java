package com.develeno.prestashopandroidclient;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ProductPageFragment extends Fragment {
    private static ArrayList<Product> productArrayList;
    /* access modifiers changed from: private */
    public Product productToView;

    public static void setProductArrayList(ArrayList<Product> productArrayList2) {
        productArrayList = productArrayList2;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_product_page, container, false);
        this.productToView = productArrayList.get(getArguments().getInt("count"));
        int howManyInCart = Cart.howManyInCart(this.productToView);
        final ImageView image = rootView.findViewById(R.id.image);
        TextView name = rootView.findViewById(R.id.pr_name);
        TextView price = rootView.findViewById(R.id.price);
        TextView unit = rootView.findViewById(R.id.quantity);
        TextView desc = rootView.findViewById(R.id.desc);
        Bitmap imageBitmap = this.productToView.getImageBitmap();
        if (imageBitmap != null) {
            image.setImageBitmap(imageBitmap);
        } else {
            NetworkConnection.fetchImageInBackground(this.productToView.getImageLink(), new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    Bitmap bitmap = (Bitmap) result;
                    ProductPageFragment.this.productToView.setImageBitmap(bitmap);
                    image.setImageBitmap(bitmap);
                    Product productById = Data.getProductByURL(ProductPageFragment.this.productToView.getURL());
                    if (productById != null) {
                        productById.setImageBitmap(bitmap);
                    }
                }

                public void refresh(Object result) {
                }
            }, false);
        }
        name.setText(this.productToView.getProductName());
        price.setText("₹" + this.productToView.getPrice());
        desc.setText(this.productToView.getDescription());
        if (this.productToView.getUnit() != null) {
            unit.setText(" - " + this.productToView.getMinQuantity() + " " + this.productToView.getUnit());
        } else {
            unit.setVisibility(View.GONE);
        }
        return rootView;
    }
}
