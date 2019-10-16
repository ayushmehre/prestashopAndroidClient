package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class AddressesActivity extends AppCompatActivity {
    private AddressAdapter addressAdapter;
    private ArrayList<Address> addresses;
    private AddressFragment fragment;
    private ListView listView;
    private View noAddressLayout;
    private SwipeRefreshLayout swipe;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle("Your Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.fragment = new AddressFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contentFrame, this.fragment).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addresses, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.add) {
            return super.onOptionsItemSelected(item);
        }
        this.fragment.addAddress();
        return true;
    }
}
