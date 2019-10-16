package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class MoreListAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public Context context;
    private ArrayList<MoreListItem> items;

    public MoreListAdapter(Context context2, ArrayList<MoreListItem> items2) {
        this.context = context2;
        this.items = items2;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int i) {
        return this.items.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        OnClickListener listener;
        RelativeLayout layout = (RelativeLayout) ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.more_list_item, viewGroup, false);
        TextView desctv = (TextView) layout.findViewById(R.id.desc);
        ImageView icon = (ImageView) layout.findViewById(R.id.icon);
        MoreListItem moreListItem = (MoreListItem) this.items.get(i);
        ((TextView) layout.findViewById(R.id.textView)).setText(moreListItem.getTitle());
        desctv.setText(moreListItem.getDesc());
        if (moreListItem.getImageId() != 0) {
            icon.setImageResource(moreListItem.getImageId());
        } else {
            icon.setVisibility(8);
        }
        final Intent intent = moreListItem.getIntent();
        if (moreListItem.getOnClickListener() == null) {
            listener = new OnClickListener() {
                public void onClick(View view) {
                    if (intent != null) {
                        MoreListAdapter.this.context.startActivity(intent);
                    }
                }
            };
        } else {
            listener = moreListItem.getOnClickListener();
        }
        layout.setOnClickListener(listener);
        return layout;
    }
}
