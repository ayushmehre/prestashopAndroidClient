package com.develeno.prestashopandroidclient;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentProductsList extends Fragment {
    private static Category parentCategory;
    private static ArrayList<Product> productsToView;
    private Category categoryToView;
    private int currentViewType;
    private GridView gridView;
    private ListView listView;
    /* access modifiers changed from: private */
    public ProductListAdapter productListAdapter;
    private ProgressBar progressBar;

    public static void setParentCategory(Category categoryToView2) {
        parentCategory = categoryToView2;
    }

    public static void setProductsToView(ArrayList<Product> productsToView2) {
        productsToView = productsToView2;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_product_list, container, false);
        this.listView = rootView.findViewById(R.id.listView);
        this.gridView = rootView.findViewById(R.id.gridView);
        this.progressBar = rootView.findViewById(R.id.progressBar);
        if (parentCategory == null || parentCategory.getChildCategories().size() <= 0) {
            this.categoryToView = parentCategory;
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                this.categoryToView = parentCategory.getChildCategories().get(bundle.getInt("count", 0));
            }
        }
        setupListView();
        return rootView;
    }

    private void setupListView() {
        ArrayList<Product> products;
        this.currentViewType = ProductListAdapter.LIST;
        this.listView.setVisibility(View.VISIBLE);
        this.gridView.setVisibility(View.GONE);
        ProductsActivity activity = ((ProductsActivity) getActivity());
        this.productListAdapter = new ProductListAdapter((AppCompatActivity) getActivity(), new ArrayList<Product>(), ProductListAdapter.LIST, activity.root);
        this.listView.setAdapter(this.productListAdapter);
        if (this.categoryToView != null) {
            products = this.categoryToView.getProducts();
        } else {
            products = productsToView;
        }
        this.progressBar.setVisibility(View.INVISIBLE);
        this.productListAdapter = new ProductListAdapter((AppCompatActivity) getActivity(), products, ProductListAdapter.LIST);
        this.listView.setAdapter(this.productListAdapter);
        this.productListAdapter.notifyDataSetChanged();
    }

    private void setupGridView() {
        ArrayList<Product> products;
        this.currentViewType = ProductListAdapter.GRID;
        this.listView.setVisibility(View.GONE);
        this.gridView.setVisibility(View.VISIBLE);
        this.productListAdapter = new ProductListAdapter((AppCompatActivity) getActivity(), new ArrayList<Product>(), ProductListAdapter.GRID);
        this.gridView.setAdapter(this.productListAdapter);
        if (this.categoryToView != null) {
            products = this.categoryToView.getProducts();
        } else {
            products = productsToView;
        }
        this.progressBar.setVisibility(View.INVISIBLE);
        this.productListAdapter = new ProductListAdapter((AppCompatActivity) getActivity(), products, ProductListAdapter.GRID);
        this.gridView.setAdapter(this.productListAdapter);
        this.productListAdapter.notifyDataSetChanged();
    }

    public void onPause() {
        this.productListAdapter.notifyDataSetChanged();
        super.onPause();
    }

    public void toggleView(MenuItem item) {
        if (this.currentViewType == 7842) {
            setupGridView();
            item.setTitle("View as List");
            return;
        }
        setupListView();
        item.setTitle("View as Grid");
    }

    public void sort(AppCompatActivity activity) {
        Builder builder = new Builder(activity);
        builder.setTitle("Sort");
        builder.setItems(new CharSequence[]{"Price high to low", "Price low to high", "Sort by Discount"}, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        FragmentProductsList.this.productListAdapter.sort(ProductListAdapter.PRICE_HIGH_TO_LOW);
                        return;
                    case 1:
                        FragmentProductsList.this.productListAdapter.sort(ProductListAdapter.PRICE_LOW_TO_HIGH);
                        return;
                    case 2:
                        FragmentProductsList.this.productListAdapter.sort(ProductListAdapter.DISCOUNT);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.show();
    }
}
