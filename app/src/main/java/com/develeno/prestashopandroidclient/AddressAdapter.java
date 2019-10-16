package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddressAdapter extends BaseAdapter {
    private final AppCompatActivity activity;
    private final ArrayList<Address> addresses;
    private final boolean selectable;
    /* access modifiers changed from: private */
    public int selected = 0;

    public AddressAdapter(ArrayList<Address> addresses2, boolean selectable2, AppCompatActivity appCompatActivity) {
        this.addresses = addresses2;
        this.activity = appCompatActivity;
        this.selectable = selectable2;
    }

    public int getCount() {
        return this.addresses.size();
    }

    public Object getItem(int i) {
        return this.addresses.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = ((LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.address_item, viewGroup, false);
        TextView line1 = layout.findViewById(R.id.line1);
        TextView line2 = layout.findViewById(R.id.line2);
        TextView code = layout.findViewById(R.id.code);
        ImageView tick = layout.findViewById(R.id.tick);
        final Address address = this.addresses.get(i);
        ((TextView) layout.findViewById(R.id.title)).setText(address.getTitle());
        String addressLine1 = address.getLine1().trim();
        if (!addressLine1.isEmpty()) {
            line1.setText(addressLine1);
        } else {
            line1.setVisibility(View.GONE);
        }
        String addressLine2 = address.getLine2().trim();
        if (!addressLine2.isEmpty()) {
            line2.setText(addressLine2);
        } else {
            line2.setVisibility(View.GONE);
        }
        String pinCode = address.getPinCode().trim();
        if (!pinCode.isEmpty()) {
            code.setText(pinCode);
        } else {
            code.setVisibility(View.GONE);
        }
        if (i != this.selected || !this.selectable) {
            tick.setVisibility(View.GONE);
        } else {
            tick.setVisibility(View.VISIBLE);
        }
        final int i2 = i;
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddressAdapter.this.selected = i2;
                AddressAdapter.this.notifyDataSetChanged();
                Data.getCurrentOrder().setAddress(address);
            }
        });
        if (Data.getCurrentOrder().getAddress() == null) {
            Data.getCurrentOrder().setAddress(address);
        }
        return layout;
    }

    public Address getSelectedAddress() {
        return this.addresses.get(this.selected);
    }
}
