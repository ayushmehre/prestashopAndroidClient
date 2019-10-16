package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import androidx.viewpager.widget.ViewPager;

public class IndicatorAdapter {
    private Context context;
    private ImageView[] dots;
    private LinearLayout indicator;
    private int selected;
    private int selectedDotID;
    private int totalDots;
    private int unselectedDotID;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    public IndicatorAdapter(int totalDots2, LinearLayout indicator2, Context context2, ViewPager viewPager2) {
        this.totalDots = totalDots2;
        this.context = context2;
        this.indicator = indicator2;
        this.viewPager = viewPager2;
        this.dots = new ImageView[totalDots2];
        this.unselectedDotID = R.drawable.dot_empty;
        this.selectedDotID = R.drawable.dot_filled;
        createDots();
        setSelected(0);
        setPageChangeListener(viewPager2);
    }

    public IndicatorAdapter(int totalDots2, LinearLayout indicator2, Context context2, ViewPager viewPager2, int unselectedDotID2, int selectedDotID2) {
        this.totalDots = totalDots2;
        this.context = context2;
        this.indicator = indicator2;
        this.viewPager = viewPager2;
        this.dots = new ImageView[totalDots2];
        this.unselectedDotID = unselectedDotID2;
        this.selectedDotID = selectedDotID2;
        createDots();
        setSelected(0);
        setPageChangeListener(viewPager2);
    }

    private void setPageChangeListener(ViewPager viewPager2) {
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                IndicatorAdapter.this.setSelected(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setSelected(int i) {
        this.dots[this.selected].setImageResource(this.unselectedDotID);
        this.selected = i;
        this.dots[i].setImageResource(this.selectedDotID);
    }

    private void createDots() {
        for (int i = 0; i < this.totalDots; i++) {
            this.dots[i] = createDot(i);
        }
    }

    private ImageView createDot(final int i) {
        ImageView imageView = new ImageView(this.context);
        int height = (int) TypedValue.applyDimension(1, 6.0f, this.context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(1, 6.0f, this.context.getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(1, 2.0f, this.context.getResources().getDisplayMetrics());
        imageView.setImageResource(this.unselectedDotID);
        LayoutParams params = new LayoutParams(width, height);
        params.setMargins(margin, margin, margin, margin);
        imageView.setLayoutParams(params);
        this.indicator.addView(imageView);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                IndicatorAdapter.this.viewPager.setCurrentItem(i);
            }
        });
        return imageView;
    }

    public void setSelectedDotID(int selectedDotID2) {
        this.selectedDotID = selectedDotID2;
    }

    public void setUnselectedDotID(int unselectedDotID2) {
        this.unselectedDotID = unselectedDotID2;
    }
}
