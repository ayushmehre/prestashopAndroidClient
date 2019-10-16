package com.develeno.prestashopandroidclient;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class WishListActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public ProductListAdapter adapter;
    /* access modifiers changed from: private */
    public ArrayList<Product> wishlist;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        this.wishlist = Data.getWishlist(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My WishList");
        setSubtitle(this.wishlist.size());
        ListView listView = findViewById(R.id.listView);
        this.adapter = new ProductListAdapter(this, this.wishlist, ProductListAdapter.LIST);
        listView.setAdapter(this.adapter);
    }

    /* access modifiers changed from: private */
    public void setSubtitle(int size) {
        View v = findViewById(R.id.empty_wishlist_layout);
        v.setVisibility(View.GONE);
        if (size > 1) {
            getSupportActionBar().setSubtitle( size + " products");
        } else if (size == 0) {
            getSupportActionBar().setSubtitle("Empty WishList");
            v.setVisibility(View.VISIBLE);
        } else if (size == 1) {
            getSupportActionBar().setSubtitle(size + " product");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wish_list, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.wishlist.size() == 0) {
            menu.getItem(0).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.clear) {
            return super.onOptionsItemSelected(item);
        }
        Builder builder = new Builder(this);
        builder.setTitle("Delete?");
        builder.setMessage("Are you sure to delete wishlist?");
        builder.setPositiveButton("Delete", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Data.setWishlist(null, WishListActivity.this);
                Toast.makeText(WishListActivity.this, "Wishlist deleted successfully", Toast.LENGTH_SHORT).show();
                WishListActivity.this.adapter.notifyDataSetChanged();
                WishListActivity.this.setSubtitle(WishListActivity.this.wishlist.size());
                WishListActivity.this.adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("cancel", null);
        if (this.wishlist.size() > 0) {
            builder.show();
        } else {
            Toast.makeText(this, "Wishlist is already empty", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
