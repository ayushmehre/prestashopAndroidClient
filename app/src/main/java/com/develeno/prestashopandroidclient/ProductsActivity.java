package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public static Category categoryToView;
    private static ArrayList<Product> productsToView;
    private ProductPageAdapter adapter;
    private FragmentProductsList fragment;
    private Toolbar mtoolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public RelativeLayout root;

    private class ProductPageAdapter extends FragmentStatePagerAdapter {
        FragmentProductsList[] frags = new FragmentProductsList[this.size];
        private final int size = ProductsActivity.categoryToView.getChildCategories().size();

        public ProductPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            FragmentProductsList fragmentProductsList = new FragmentProductsList();
            Bundle bundle = new Bundle();
            bundle.putInt("count", position);
            fragmentProductsList.setArguments(bundle);
            FragmentProductsList.setParentCategory(ProductsActivity.categoryToView);
            this.frags[position] = fragmentProductsList;
            return fragmentProductsList;
        }

        public CharSequence getPageTitle(int position) {
            return ProductsActivity.categoryToView.getChildCategories().get(position).getLabel();
        }

        public int getCount() {
            return this.size;
        }
    }

    public static void setCategoryToView(Category categoryToView2) {
        categoryToView = categoryToView2;
    }

    public static void setProductsToView(ArrayList<Product> productsToView2) {
        productsToView = productsToView2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        root = findViewById(R.id.root);
        initializeViews();
        setupActionBar();
        if (categoryToView == null || categoryToView.getChildCategories().size() <= 0) {
            this.fragment = new FragmentProductsList();
            if (productsToView == null) {
                FragmentProductsList.setParentCategory(categoryToView);
            } else {
                FragmentProductsList.setProductsToView(productsToView);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.contentFrame, this.fragment).commit();
            this.tabLayout.setVisibility(View.GONE);
            return;
        }
        if (productsToView != null && productsToView.size() > 0) {
            setupViewPager();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        categoryToView = null;
        productsToView = null;
        super.onDestroy();
    }

    private void setupViewPager() {
        this.adapter = new ProductPageAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setOffscreenPageLimit(10);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        this.tabLayout.setSelectedTabIndicatorHeight(0);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                ProductsActivity.this.setSubtitle(ProductsActivity.categoryToView.getChildCategories().get(position).getProducts().size());
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(this.mtoolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (categoryToView != null) {
            supportActionBar.setTitle(categoryToView.getLabel());
            setSubtitle(categoryToView.getProducts().size());
            return;
        }
        supportActionBar.setTitle(getIntent().getStringExtra("title"));
    }

    /* access modifiers changed from: private */
    public void setSubtitle(int size) {
        if (size > 1) {
            getSupportActionBar().setSubtitle(size + " products");
        } else if (size == 0) {
            getSupportActionBar().setSubtitle("Empty Category");
        } else if (size == 1) {
            getSupportActionBar().setSubtitle((size + " product"));
        }
    }

    private void initializeViews() {
        this.mtoolbar = findViewById(R.id.toolbar);
        this.viewPager = findViewById(R.id.pager);
        this.tabLayout = findViewById(R.id.tabs);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (id == R.id.search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.view) {
            if (this.fragment != null) {
                this.fragment.toggleView(item);
                return true;
            }
            this.adapter.frags[this.viewPager.getCurrentItem()].toggleView(item);
            return true;
        } else if (id != R.id.sort) {
            return super.onOptionsItemSelected(item);
        } else {
            if (this.fragment != null) {
                this.fragment.sort(this);
                return true;
            }
            this.adapter.frags[this.viewPager.getCurrentItem()].sort(this);
            return true;
        }
    }
}
