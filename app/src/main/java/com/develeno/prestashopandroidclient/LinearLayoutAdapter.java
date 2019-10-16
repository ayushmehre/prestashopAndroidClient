package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class LinearLayoutAdapter {
    private BaseAdapter adapter;
    private Context context;
    private int count;
    private int margin;
    private ViewGroup viewGroup;
    private float width;
    private int widthpx;

    public LinearLayoutAdapter(LinearLayout viewGroup2, BaseAdapter adapter2, Context context2) {
        int adapterCount = adapter2.getCount();
        this.adapter = adapter2;
        this.viewGroup = viewGroup2;
        this.context = context2;
        viewGroup2.removeAllViews();
        if (adapterCount > 15) {
            adapterCount = 15;
        }
        this.count = adapterCount;
        this.width = 110.0f;
        Resources r = context2.getResources();
        this.widthpx = (int) TypedValue.applyDimension(1, this.width, r.getDisplayMetrics());
        this.margin = (int) TypedValue.applyDimension(1, 8.0f, r.getDisplayMetrics());
    }

    public void createViews() {
        for (int i = 0; i < this.count; i++) {
            View view = this.adapter.getView(i, null, this.viewGroup);
            LayoutParams params = new LayoutParams(this.widthpx, -2);
            params.setMargins(this.margin / 2, this.margin, this.margin / 2, this.margin);
            view.setLayoutParams(params);
            this.viewGroup.addView(view);
        }
    }

    public void setWidth(float width2) {
        this.width = width2;
        this.widthpx = (int) TypedValue.applyDimension(1, width2, this.context.getResources().getDisplayMetrics());
    }
}
