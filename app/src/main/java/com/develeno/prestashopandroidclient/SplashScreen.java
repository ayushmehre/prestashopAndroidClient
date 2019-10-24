package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 2000;

    public boolean hasSoftKeys() {
        Context c = getApplicationContext();
        if (VERSION.SDK_INT >= 17) {
            Display d = getWindowManager().getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            return realWidth - displayMetrics.widthPixels > 0 || realHeight - displayHeight > 0;
        }
        return !ViewConfiguration.get(c).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(4);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setupBottomBar();
       // FacebookSdk.sdkInitialize(getApplicationContext());
        if (Data.getCurrentUser(this) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        final RelativeLayout layout = findViewById(R.id.relativeLayout);
        final ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setOverScrollMode(2);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new SplashScreenPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 3) {
                    layout.setVisibility(View.GONE);
                    //hideNavigationBar();
                } else {
                    layout.setVisibility(View.VISIBLE);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        new IndicatorAdapter(4, (LinearLayout) findViewById(R.id.indicator), this, viewPager, R.drawable.dot_transparent, R.drawable.dot_white);
        findViewById(R.id.next_layout).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });
        findViewById(R.id.skip).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getChildCount() - 1, true);
            }
        });
        //hideNavigationBar();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setupBottomBar() {
        if (!hasSoftKeys()) {
            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        //AppEventsLogger.activateApp(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        //AppEventsLogger.deactivateApp(this);
    }

    private void hideNavigationBar() {
        if (VERSION.SDK_INT >= 19) {
            Window w = getWindow();
            w.setFlags(134217728, 134217728);
            w.setFlags(67108864, 67108864);
        }
    }

    private void waitSomeTime() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
                SplashScreen.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void start(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void signIn(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
