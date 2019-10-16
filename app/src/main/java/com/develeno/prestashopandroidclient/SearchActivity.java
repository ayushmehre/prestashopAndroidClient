package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private RelativeLayout noResultsLayout;
    private ArrayList<Product> productsResults;
    private ProgressBar progressBar;
    private SearchListAdapter searchListAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.listView = findViewById(R.id.listView);
        this.progressBar = findViewById(R.id.progressBar3);
        this.noResultsLayout = findViewById(R.id.no_results_layout);
        this.productsResults = new ArrayList<>();
        this.searchListAdapter = new SearchListAdapter(this.productsResults, this);
        this.listView.setAdapter(this.searchListAdapter);
        ((EditText) findViewById(R.id.search_bar_edittext)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchActivity.this.search(charSequence);
            }

            public void afterTextChanged(Editable editable) {
            }
        });
        findViewById(R.id.up).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SearchActivity.this.onBackPressed();
            }
        });
    }

    /* access modifiers changed from: private */
    public void search(CharSequence charSequence) {
        this.productsResults.clear();
        this.progressBar.setVisibility(View.VISIBLE);
        this.noResultsLayout.setVisibility(View.GONE);
        this.listView.setVisibility(View.GONE);
        Iterator it = Data.getProducts().iterator();
        while (it.hasNext()) {
            Product product = (Product) it.next();
            if (product != null && product.getProductName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                this.productsResults.add(product);
            }
            this.searchListAdapter.notifyDataSetChanged();
        }
        if (this.productsResults.size() > 0) {
            this.progressBar.setVisibility(View.GONE);
            this.noResultsLayout.setVisibility(View.GONE);
            this.listView.setVisibility(View.VISIBLE);
            return;
        }
        this.progressBar.setVisibility(View.GONE);
        this.noResultsLayout.setVisibility(View.VISIBLE);
        this.listView.setVisibility(View.GONE);
    }
}
