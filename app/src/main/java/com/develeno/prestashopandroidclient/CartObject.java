package com.develeno.prestashopandroidclient;

import java.util.ArrayList;

public class CartObject {

    /* renamed from: id */
    private int f21id;
    private ArrayList<CartItem> items = new ArrayList<>();

    public int getId() {
        return this.f21id;
    }

    public void setId(int id) {
        this.f21id = id;
    }

    public ArrayList<CartItem> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<CartItem> items2) {
        this.items = items2;
    }

    public void additem(CartItem item) {
        this.items.add(item);
    }
}
