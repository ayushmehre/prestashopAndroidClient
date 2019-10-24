package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class Data {
    private static ArrayList<Address> addresses;
    private static ArrayList<Category> categories = new ArrayList<>();
    private static Order currentOrder = new Order(null, null, null);
    private static Customer currentUser = null;
    private static ArrayList<FragmentHome.SliderOffer> offers = new ArrayList<>();
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Product> recents = new ArrayList<>(15);
    private static ArrayList<StockInfo> stockInfos = new ArrayList<>();
    private static ArrayList<Product> wishlist = new ArrayList<>();

    public static ArrayList<Category> getCategories() {
        return categories;
    }

    public static void setCategories(ArrayList<Category> categories2) {
        categories = categories2;
        organiseCategories();
    }

    public static void organiseCategories() {
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            Category category = (Category) it.next();
            if (category.getDepth() > 0) {
                category.setParentCategory(getCategoryByURL(category.getParentCategoryURL()));
            }
        }
        Iterator it2 = categories.iterator();
        while (it2.hasNext()) {
            Category parentCategory = (Category) it2.next();
            Iterator it3 = categories.iterator();
            while (it3.hasNext()) {
                Category category2 = (Category) it3.next();
                if (category2.getParentCategoryURL() != null && category2.isParentOf(parentCategory)) {
                    parentCategory.addToChildCategories(category2);
                }
            }
        }
    }

    public static ArrayList<Product> getProducts() {
        return products;
    }

    public static void setProducts(ArrayList<Product> products2) {
        products = products2;
        organiseProductsIntoCategories();
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x000c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    //private static void organiseProductsIntoCategories() {
        /*
            java.util.ArrayList<com.develeno.prestashopandroidclient.Product> r1 = products
            java.util.Iterator r1 = r1.iterator()
        L_0x0006:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0015
            java.lang.Object r0 = r1.next()
            com.develeno.prestashopandroidclient.Product r0 = (com.develeno.prestashopandroidclient.Product) r0
            if (r0 == 0) goto L_0x0006
            goto L_0x0006
        L_0x0015:
            return
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.develeno.prestashopandroidclient.Data.organiseProductsIntoCategories():void");
    //}

    private static void organiseProductsIntoCategories() {
//        for (Category product: products) {
//            if(categories.get(0).){
//
//            }
//        }
    }

    private static Category getCategoryById(Integer id) {
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            Category category = (Category) it.next();
            if (category != null && category.getId() == id.intValue()) {
                return category;
            }
        }
        return null;
    }

    public static Category getCategoryByURL(String URL) {
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            Category category = (Category) it.next();
            if (category.getURL().contentEquals(URL)) {
                return category;
            }
        }
        return null;
    }

    public static Product getProductByURL(String URL) {
        try {
            Iterator it = products.iterator();
            while (it.hasNext()) {
                Product product = (Product) it.next();
                if (product.getURL().contentEquals(URL)) {
                    return product;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Customer getCurrentUser(Context context) {
        if (currentUser != null) {
            return currentUser;
        }
        currentUser = new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString("current", null), Customer.class);
        return currentUser;
    }

    public static boolean isFirstTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean first = preferences.getBoolean("first", true);
        if (first) {
            preferences.edit().putBoolean("first", false).apply();
        }
        return first;
    }

    public static void setCurrentUser(Customer currentUser2, Context context) {
        currentUser = currentUser2;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("current", new Gson().toJson(currentUser2)).apply();
    }

    public static void saveData(Context context) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        String[] strings = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            strings[i] = new Gson().toJson(categories.get(i));
        }
        editor.putStringSet("categories", new HashSet<>(Arrays.asList(strings)));
        editor.apply();
    }

    public static void loadData(Context context) {
    }

    public static ArrayList<FragmentHome.SliderOffer> getOffers() {
        return offers;
    }

    public static void setOffers(ArrayList<FragmentHome.SliderOffer> offers2) {
        offers = offers2;
    }

    public static ArrayList<Address> getAddresses() {
        return addresses;
    }

    public static void setAddresses(ArrayList<Address> addresses2) {
        addresses = addresses2;
    }

    public static void addToAddresses(Address address1) {
        addresses.add(address1);
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public static ArrayList<Product> getWishlist(Context context) {
        wishlist.clear();
        Set<String> set = PreferenceManager.getDefaultSharedPreferences(context).getStringSet("wishlist", null);
        if (set != null) {
            String[] urls = new String[set.size()];
            set.toArray(urls);
            for (String url : urls) {
                wishlist.add(getProductByURL(url));
            }
        }
        return wishlist;
    }

    public static void setWishlist(ArrayList<Product> wishlist2, Context context) {
        if (wishlist2 != null) {
            wishlist = wishlist2;
            return;
        }
        wishlist.clear();
        saveWishList(context);
    }

    private static void saveWishList(Context context) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        Set<String> set = new HashSet<>();
        Iterator it = wishlist.iterator();
        while (it.hasNext()) {
            set.add(((Product) it.next()).getURL());
        }
        editor.putStringSet("wishlist", set).apply();
    }

    public static void addToWishList(Product product, Context context) {
        wishlist.add(product);
        saveWishList(context);
    }

    public static void removeFromWishList(Product product, Context context) {
        for (int i = 0; i < wishlist.size(); i++) {
            if (wishlist.get(i).getURL().contentEquals(product.getURL())) {
                wishlist.remove(i);
            }
        }
        saveWishList(context);
    }

    public static boolean isThisInWishList(Product product) {
        Iterator it = wishlist.iterator();
        while (it.hasNext()) {
            if (((Product) it.next()).getURL().contentEquals(product.getURL())) {
                return true;
            }
        }
        return false;
    }

    private static void saveRecents(Context context) {
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            Set<String> set = new LinkedHashSet<>();
            Iterator it = recents.iterator();
            while (it.hasNext()) {
                set.add(((Product) it.next()).getURL());
            }
            editor.putStringSet("recents", set).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addToRecents(Product product, Context context) {
        recents.add(0, product);
        saveRecents(context);
    }

    public static ArrayList<Product> getRecentProducts(Context context) {
        recents.clear();
        Set<String> set = PreferenceManager.getDefaultSharedPreferences(context).getStringSet("recents", null);
        if (set != null) {
            String[] urls = new String[set.size()];
            set.toArray(urls);
            for (String url : urls) {
                recents.add(getProductByURL(url));
            }
        }
        return recents;
    }

    public static ArrayList<StockInfo> getStockInfos() {
        return stockInfos;
    }

    public static void setStockInfos(ArrayList<StockInfo> stockInfos2) {
        stockInfos = stockInfos2;
    }

    public static StockInfo getStockInfoByProductId(int id) {
        Iterator it = stockInfos.iterator();
        while (it.hasNext()) {
            StockInfo info = (StockInfo) it.next();
            if (info.getProductId() == id) {
                return info;
            }
        }
        return null;
    }

    public static void clearCurrentOrder() {
        currentOrder = new Order(null, null, null);
    }

    public static String getLoadingText() {
        String[] txts = {"Share Your order on Facebook and get flat 5% discount", "Give us 5 star rating on play store to show support", "Email us any suggestions, feedback or query at vegetableshoppy@gmail.com", "Sort list of products by tapping on options button", "See all products in grid view by tapping on options button", "Add products to your wish list to quickly access them", "Report any error if you encounter them by tapping report button", "For any Queries call us at 0761-6444555"};
        return txts[new Random().nextInt(txts.length)];
    }

    public static void saveLocally(Bitmap bitmap, String id) {
        String root = Environment.getExternalStorageDirectory().toString();
        String id2 = id.replace("/", "").replace(":", "").replace(".", "");
        File file = new File(root + "/" + id2 + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d("CACHE", "saved " + id2);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CACHE", e.getMessage());
        }
    }

    public static boolean isAvailLocally(String id) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            return new File(root + "/" + id.replace("/", "").replace(":", "").replace(".", "") + ".jpg").exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileIfAvailLocally(String id) {
        String root = Environment.getExternalStorageDirectory().toString();
        return new File(root + "/" + id.replace("/", "").replace(":", "").replace(".", "") + ".jpg").getAbsolutePath();
    }
}
