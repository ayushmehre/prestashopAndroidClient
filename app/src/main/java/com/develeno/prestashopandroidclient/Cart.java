package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;

public class Cart {
    public static final int DELIVERYCHARGES = 15;
    public static final String ERROR_MAX_REACHED = "Can not purchase more than 10 items at once";
    public static final int FREEDELIVERYAMOUNT = 199;
    private static ArrayList<CartItem> items = new ArrayList<>();

    public static void clear() {
        items = new ArrayList<>();
    }

    public static boolean addItems(CartItem cartItem, Context context) {
        boolean condition;
        if (!isInCart(cartItem.getItem())) {
            items.add(cartItem);
            return true;
        }
        if (howManyInCart(cartItem.getItem()) + cartItem.getQuantity() <= 10) {
            condition = true;
        } else {
            condition = false;
        }
        if (condition) {
            getItem(cartItem).increase(cartItem.getQuantity());
            return true;
        }
        prompt(ERROR_MAX_REACHED, context);
        return false;
    }

    public static boolean removeItems(Product product, int i) {
        if (isInCart(product)) {
            CartItem item = getItem(product);
            if (item.getQuantity() > 0) {
                item.increase(-1);
                if (item.getQuantity() == 0) {
                    items.remove(item);
                }
            }
        }
        return true;
    }

    private static void prompt(String s, Context context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private static CartItem getItem(CartItem cartItem) {
        Iterator it = items.iterator();
        while (it.hasNext()) {
            CartItem item = (CartItem) it.next();
            if (item.getItem().getURL().contentEquals(cartItem.getItem().getURL())) {
                return item;
            }
        }
        return null;
    }

    private static CartItem getItem(Product product) {
        Iterator it = items.iterator();
        while (it.hasNext()) {
            CartItem item = (CartItem) it.next();
            if (item.getItem().getURL().contentEquals(product.getURL())) {
                return item;
            }
        }
        return null;
    }

    public static ArrayList<CartItem> getItems() {
        return items;
    }

    public static double getTotal() {
        double total = 0.0d;
        Iterator it = getItems().iterator();
        while (it.hasNext()) {
            CartItem cartItem = (CartItem) it.next();
            total += cartItem.getItem().getPrice() * ((double) cartItem.getQuantity());
        }
        return total;
    }

    public static int howManyInCart(Product product) {
        if (!isInCart(product)) {
            return 0;
        }
        Iterator it = items.iterator();
        while (it.hasNext()) {
            CartItem cartItem = (CartItem) it.next();
            if (cartItem.getItem().getURL().contentEquals(product.getURL())) {
                return cartItem.getQuantity();
            }
        }
        return 0;
    }

    public static boolean isInCart(Product product) {
        Iterator it = items.iterator();
        while (it.hasNext()) {
            if (((CartItem) it.next()).getItem().getURL().contentEquals(product.getURL())) {
                return true;
            }
        }
        return false;
    }
}
