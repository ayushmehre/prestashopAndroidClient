package com.develeno.prestashopandroidclient;

import java.util.ArrayList;

public class Order {
    private Address address;
    private int cartId;
    private double discount;
    private boolean isDiscountProvided;
    private ArrayList<CartItem> items;
    private OnOrderChangeListener onOrderChangeListener;
    private String paymentMethod = "Cash on delivery (COD)";
    private String timeslot;

    public Order(ArrayList<CartItem> items2, Address address2, String timeslot2) {
        this.address = address2;
        this.timeslot = timeslot2;
        this.items = items2;
    }

    public double getDiscount() {
        return this.discount;
    }

    public String getTimeslot() {
        return this.timeslot;
    }

    public void setTimeslot(String timeslot2) {
        this.timeslot = timeslot2;
        update();
    }

    private void update() {
        if (this.onOrderChangeListener != null) {
            this.onOrderChangeListener.onTimeChanged(this);
        }
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address2) {
        this.address = address2;
        update();
    }

    public ArrayList<CartItem> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<CartItem> items2) {
        this.items = items2;
        update();
    }

    public void setOnOrderChangeListener(OnOrderChangeListener onOrderChangeListener2) {
        this.onOrderChangeListener = onOrderChangeListener2;
    }

    public int getCarrierId() {
        String str = this.timeslot;
        char c = 65535;
        switch (str.hashCode()) {
            case 1521729346:
                if (str.equals(SelectTimeFragment.TIME_SLOT_1)) {
                    c = 0;
                    break;
                }
                break;
            case 1521729347:
                if (str.equals(SelectTimeFragment.TIME_SLOT_2)) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 9;
            case 1:
                return 11;
            default:
                return 0;
        }
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public double getTotalAmountPayable() {
        double total = Cart.getTotal() - this.discount;
        return total < 199.0d ? total + 15.0d : total;
    }

    public int getCartId() {
        return this.cartId;
    }

    public void setCartId(int cartId2) {
        this.cartId = cartId2;
    }

    public void provideDiscount(double i) {
        this.discount = Cart.getTotal() * (i / 100.0d);
        this.isDiscountProvided = true;
        update();
    }

    public boolean isDiscountProvided() {
        return this.isDiscountProvided;
    }
}
