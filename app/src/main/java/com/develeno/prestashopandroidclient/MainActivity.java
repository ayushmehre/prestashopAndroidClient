package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static MainActivity mainActivity;
    private OnGotResponse listener;
    private Toolbar mtoolbar;
    private ViewPager pager;
    private TabLayout tabLayout;

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private CharSequence[] Titles = {"Home", "Categories", "More"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentHome();
                case 1:
                    return new FragmentCategories();
                case 2:
                    return new FragmentMore();
                default:
                    return new FragmentHome();
            }
        }

        public CharSequence getPageTitle(int position) {
            return this.Titles[position];
        }

        public int getCount() {
            return 3;
        }
    }

    public static void finishActivty() {
        if (mainActivity != null) {
            mainActivity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        setupActionBar();
        setupViewPager();
        mainActivity = this;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    private void setupViewPager() {
        this.pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        this.tabLayout.setSmoothScrollingEnabled(true);
        this.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        this.tabLayout.setupWithViewPager(this.pager);
        this.pager.setOffscreenPageLimit(5);
    }

    private void initializeViews() {
        this.mtoolbar = findViewById(R.id.toolbar);
        this.pager = findViewById(R.id.pager);
        this.tabLayout = findViewById(R.id.tabs);
    }

    private void setupActionBar() {
        setSupportActionBar(this.mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Vegetable Shoppy");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart /*2131689649*/:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.search /*2131689800*/:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setOnCategoriesAndProductsLoadedListener(OnGotResponse listener2) {
        this.listener = listener2;
    }

    public void setCurrentItem(int i) {
        this.pager.setCurrentItem(i);
    }

    public OnGotResponse getListener() {
        return this.listener;
    }
}
