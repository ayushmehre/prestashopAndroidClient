package com.develeno.prestashopandroidclient;

class CartItem {
    private Product item;
    private int quantity;

    public CartItem(Product item2, int quantity2) {
        this.item = item2;
        this.quantity = quantity2;
    }

    public Product getItem() {
        return this.item;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void increase(int i) {
        this.quantity += i;
    }
}
