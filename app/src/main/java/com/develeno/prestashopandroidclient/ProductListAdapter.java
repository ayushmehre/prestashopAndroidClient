package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ProductListAdapter extends BaseAdapter {
    public static final int DISCOUNT = 65665;
    public static final int GRID = 6544;
    public static final int LIST = 7842;
    public static final int PRICE_HIGH_TO_LOW = 4654;
    public static final int PRICE_LOW_TO_HIGH = 65464;
    /* access modifiers changed from: private */
    public AppCompatActivity activity;
    /* access modifiers changed from: private */
    public ArrayList<Product> products;
    private final int viewType;
    private View activity_layout;

    public ProductListAdapter(AppCompatActivity activity2, ArrayList<Product> products2, int viewType2) {
        this.activity = activity2;
        this.products = products2;
        this.viewType = viewType2;
        removeNullProductObjects();
    }

    public ProductListAdapter(AppCompatActivity activity2, ArrayList<Product> products2, int viewType2, View activity_layout) {
        this.activity = activity2;
        this.products = products2;
        this.viewType = viewType2;
        removeNullProductObjects();
        this.activity_layout = activity_layout;
    }
    private void removeNullProductObjects() {
        ArrayList<Product> filtered = new ArrayList<>();
        Iterator it = this.products.iterator();
        while (it.hasNext()) {
            Product product = (Product) it.next();
            if (product != null) {
                filtered.add(product);
            }
        }
        this.products = filtered;
        notifyDataSetChanged();
    }

    private void startFetchingAllImages(ArrayList<Product> products2) {
        Iterator it = products2.iterator();
        while (it.hasNext()) {
            final Product product = (Product) it.next();
            if (product.getImageBitmap() == null) {
                NetworkConnection.fetchImageInBackground(product.getImageLink(), new OnBackgroundTaskCompleted() {
                    public void getResult(Object result) {
                        Bitmap bitmap = (Bitmap) result;
                        product.setImageBitmap(bitmap);
                        Product productByURL = Data.getProductByURL(product.getURL());
                        if (productByURL != null) {
                            productByURL.setImageBitmap(bitmap);
                        }
                    }

                    public void refresh(Object result) {
                    }
                }, true);
            }
        }
    }

    public int getCount() {
        return this.products.size();
    }

    public Object getItem(int i) {
        return this.products.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout layout;
        ImageView image;
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Product product = this.products.get(i);
        if (this.viewType == 7842) {
            layout = (RelativeLayout) inflater.inflate(R.layout.product_list_item, viewGroup, false);
            if (product == null) {
                layout.setVisibility(View.GONE);
                return layout;
            }
            TextView price = layout.findViewById(R.id.price);
            TextView min = layout.findViewById(R.id.quantity);
            TextView sale = layout.findViewById(R.id.sale);
            image = layout.findViewById(R.id.icon);
            ((TextView) layout.findViewById(R.id.name)).setText(product.getProductName() + "");
            price.setText("₹ " + product.getPrice());
            if (product.getUnit() != null) {
                min.setText(" - " + product.getMinQuantity() + " " + product.getUnit());
            } else {
                min.setVisibility(View.INVISIBLE);
            }
            if (product.isOnSale()) {
                sale.setVisibility(View.VISIBLE);
            } else {
                sale.setVisibility(View.GONE);
            }
            setupAddToCartView(layout, product);
        } else {
            layout = (RelativeLayout) inflater.inflate(R.layout.product_grid_item, viewGroup, false);
            if (product == null) {
                layout.setVisibility(View.GONE);
                return layout;
            }
            TextView price2 = layout.findViewById(R.id.price);
            TextView min2 = layout.findViewById(R.id.quantity);
            TextView textView = layout.findViewById(R.id.sale);
            TextView textView2 = layout.findViewById(R.id.count);
            ImageView imageView = layout.findViewById(R.id.add_button);
            ImageView imageView2 = layout.findViewById(R.id.remove_button);
            image = layout.findViewById(R.id.icon);
            ((TextView) layout.findViewById(R.id.name)).setText(product.getProductName());
            price2.setText("₹" + product.getPrice());
            if (product.getUnit() != null) {
                min2.setText(" - " + product.getMinQuantity() + " " + product.getUnit());
            } else {
                min2.setVisibility(View.INVISIBLE);
            }
        }
        if (product.getImageBitmap() == null) {
            setImageInBackground(product, image);
        } else {
            image.setImageBitmap(product.getImageBitmap());
        }
        final int i2 = i;
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductPageActivity.setProductArrayList(ProductListAdapter.this.products, i2);
                ProductListAdapter.this.activity.startActivity(new Intent(ProductListAdapter.this.activity, ProductPageActivity.class));
            }
        });
        return layout;
    }

    private void setupAddToCartView(RelativeLayout layout, Product product) {
        LinearLayout linearLayout = layout.findViewById(R.id.linearLayout);
        ImageView add = layout.findViewById(R.id.add_button);
        ImageView remove = layout.findViewById(R.id.remove_button);
        final TextView count = layout.findViewById(R.id.count);
        TextView outOfStocks = layout.findViewById(R.id.outofstocks);
        if (product.isAvailInStocks()) {
            final int[] countInt = {Cart.howManyInCart(product)};
            count.setText(countInt[0] + "");
            final Product product2 = product;
            final RelativeLayout relativeLayout = layout;
            add.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (countInt[0] < 10) {
                        int[] iArr = countInt;
                        iArr[0] = iArr[0] + 1;
                        count.setText(countInt[0] + "");
                        if (Cart.addItems(new CartItem(product2, 1), ProductListAdapter.this.activity)) {
                           /* Snackbar.make(activity_layout, product2.getProductName() + " added to cart", Snackbar.LENGTH_LONG).setAction("VIEW CART", new OnClickListener() {
                                public void onClick(View view) {
                                    ProductListAdapter.this.activity.startActivity(new Intent(ProductListAdapter.this.activity, CartActivity.class));
                                }
                            }).show();*/
                            Toast.makeText(activity, product2.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            final Product product3 = product;
            final RelativeLayout relativeLayout2 = layout;
            remove.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (countInt[0] > 0) {
                        int[] iArr = countInt;
                        iArr[0] = iArr[0] - 1;
                        count.setText(countInt[0] + "");
                        /*if (Cart.removeItems(product3, 1)) {
                            Snackbar.make(relativeLayout2, product3.getProductName() + " removed from cart", Snackbar.LENGTH_LONG).setAction("VIEW CART", new OnClickListener() {
                                public void onClick(View view) {
                                    ProductListAdapter.this.activity.startActivity(new Intent(ProductListAdapter.this.activity, CartActivity.class));
                                }
                            }).show();
                        }*/
                        Toast.makeText(activity, product2.getProductName() + " removed from cart", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return;
        }
        outOfStocks.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
    }

    private void setImageInBackground(final Product product, final ImageView image) {
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

    public void sort(int choice) {
        switch (choice) {
            case PRICE_HIGH_TO_LOW /*4654*/:
                Collections.sort(this.products, new Comparator<Product>() {
                    public int compare(Product result1, Product result2) {
                        if (result1.getPrice() > result2.getPrice()) {
                            return -1;
                        }
                        if (result1.getPrice() > result2.getPrice()) {
                            return 1;
                        }
                        return 0;
                    }
                });
                Toast.makeText(this.activity, "Sorted by price high to low", Toast.LENGTH_SHORT).show();
                break;
            case PRICE_LOW_TO_HIGH /*65464*/:
                Collections.sort(this.products, new Comparator<Product>() {
                    public int compare(Product result1, Product result2) {
                        if (result1.getPrice() > result2.getPrice()) {
                            return 1;
                        }
                        if (result1.getPrice() > result2.getPrice()) {
                            return -1;
                        }
                        return 0;
                    }
                });
                Toast.makeText(this.activity, "Sorted by price low to high", Toast.LENGTH_SHORT).show();
                break;
            case DISCOUNT /*65665*/:
                Collections.sort(this.products, new Comparator<Product>() {
                    public int compare(Product result1, Product result2) {
                        if (result1.isOnSale()) {
                            return -1;
                        }
                        return 1;
                    }
                });
                Toast.makeText(this.activity, "Sorted by discount", Toast.LENGTH_SHORT).show();
                break;
        }
        notifyDataSetChanged();
    }
}
