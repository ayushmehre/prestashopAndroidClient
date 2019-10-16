package com.develeno.prestashopandroidclient;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    private static CartActivity instance;
    /* access modifiers changed from: private */
    public CartListAdapter adapter;
    private int cartSize;
    private RelativeLayout checkoutLayout;
    private LinearLayout emptyCartLayout;
    private TextView freeDelivery;
    private ListView listView;
    private Toolbar toolbar;
    private TextView total;

    public static void finishActivity() {
        if (instance != null) {
            instance.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        instance = this;
        this.cartSize = Cart.getItems().size();
        initViews();
        setupActionBar();
        if (this.cartSize == 0) {
            makeEmptyLayoutVisible();
            return;
        }
        this.emptyCartLayout.setVisibility(View.GONE);
        this.adapter = new CartListAdapter(this, Cart.getItems());
        this.listView.setAdapter(this.adapter);
        calcTotal();
    }

    public void calcTotal() {
        double total2 = Cart.getTotal();
        this.total.setText("₹" + new DecimalFormat("#.##").format(total2));
        if (total2 < 199.0d) {
            this.freeDelivery.setText("Shop for more than ₹ 150 & get Free Delivery");
            this.freeDelivery.setTextColor(Color.parseColor("#e3423f"));
            return;
        }
        this.freeDelivery.setText("Hurray! Free Delivery");
        this.freeDelivery.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void initViews() {
        this.toolbar = findViewById(R.id.toolbar);
        this.listView = findViewById(R.id.listview);
        this.checkoutLayout = findViewById(R.id.checkout);
        this.emptyCartLayout = findViewById(R.id.empty_cart_layout);
        this.total = findViewById(R.id.total);
        this.freeDelivery = findViewById(R.id.freedelivery);
    }

    public void setupActionBar() {
        this.cartSize = Cart.getItems().size();
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");
        switch (this.cartSize) {
            case 0:
                getSupportActionBar().setSubtitle("Empty Cart");
                return;
            case 1:
                getSupportActionBar().setSubtitle("1 item");
                return;
            default:
                getSupportActionBar().setSubtitle(this.cartSize + " items");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.clear_cart) {
            return super.onOptionsItemSelected(item);
        }
        Builder builder = new Builder(this);
        builder.setTitle("Clear cart?");
        builder.setMessage("Do you really want to delete all items from cart?");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Clear cart", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Cart.clear();
                CartActivity.this.adapter.notifyDataSetChanged();
                CartActivity.this.makeEmptyLayoutVisible();
            }
        });
        builder.create().show();
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.cartSize == 0) {
            menu.removeItem(R.id.clear_cart);
        }
        return true;
    }

    public void makeEmptyLayoutVisible() {
        this.freeDelivery.setVisibility(View.GONE);
        this.listView.setVisibility(View.GONE);
        this.checkoutLayout.setVisibility(View.GONE);
        this.emptyCartLayout.setVisibility(View.VISIBLE);
        setupActionBar();
    }

    public void continueShopping(View view) {
        finish();
    }

    public void checkOut(View view) {
        if (Data.getCurrentUser(this) == null) {
            Toast.makeText(this, "Sign in to place order", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.putExtra("exitToCheckOutPage", true);
            startActivity(intent);
            return;
        }
        startActivity(new Intent(this, CheckoutActivity.class));
    }
}
