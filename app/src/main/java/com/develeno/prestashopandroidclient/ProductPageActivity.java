package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import org.apache.http.protocol.HTTP;


public class ProductPageActivity extends AppCompatActivity {
    public static ArrayList<Product> productArrayList;
    private static int productToView;
    private ProductPageAdapter adapter;
    private ImageView addButton;
    private TextView addToCart;
    private View addToCartView;
    private ImageView cartButton;
    /* access modifiers changed from: private */
    public Product currentProduct;
    private ImageView favoriteButton;
    private View outOfStocks;
    /* access modifiers changed from: private */
    public ViewPager pager;
    /* access modifiers changed from: private */
    public int quantity;
    /* access modifiers changed from: private */
    public TextView quantityTv;
    private ImageView removeButton;
    private ImageView shareButton;

    private class ProductPageAdapter extends FragmentStatePagerAdapter {
        int count;
        private Fragment[] fragment = new Fragment[2];

        public ProductPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            ProductPageFragment.setProductArrayList(ProductPageActivity.productArrayList);
            ProductPageFragment productPageFragment = new ProductPageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("count", position);
            productPageFragment.setArguments(bundle);
            if (this.count == 0) {
                this.fragment[1] = productPageFragment;
                this.count = 1;
            } else {
                this.fragment[0] = productPageFragment;
                this.count = 0;
            }
            return productPageFragment;
        }

        public int getCount() {
            return ProductPageActivity.productArrayList.size();
        }
    }

    public static void setProductArrayList(ArrayList<Product> productArrayList2, int productToView2) {
        productArrayList = productArrayList2;
        productToView = productToView2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        this.quantity = 1;
        this.currentProduct = productArrayList.get(productToView);
        Data.addToRecents(this.currentProduct, this);
        this.pager = findViewById(R.id.pager);
        this.addButton = findViewById(R.id.add_button);
        this.removeButton = findViewById(R.id.remove_button);
        this.shareButton = findViewById(R.id.share);
        this.cartButton = findViewById(R.id.cart);
        this.favoriteButton = findViewById(R.id.fav);
        this.addToCart = findViewById(R.id.add_to_cart);
        this.quantityTv = findViewById(R.id.count);
        this.addToCartView = findViewById(R.id.add_to_cart_view);
        this.outOfStocks = findViewById(R.id.outofstocks);
        this.adapter = new ProductPageAdapter(getSupportFragmentManager());
        this.pager.setAdapter(this.adapter);
        this.pager.setCurrentItem(productToView, false);
        this.quantityTv.setText(this.quantity + "");
        this.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                ProductPageActivity.this.currentProduct = ProductPageActivity.productArrayList.get(position);
                Data.addToRecents(ProductPageActivity.this.currentProduct, ProductPageActivity.this);
                ProductPageActivity.this.updateActionBarButtons();
                ProductPageActivity.this.updateAddToCartView();
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductPageActivity.this.quantity < 10) {
                    ProductPageActivity.this.quantity = ProductPageActivity.this.quantity + 1;
                    ProductPageActivity.this.quantityTv.setText(ProductPageActivity.this.quantity + "");
                }
            }
        });
        this.removeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductPageActivity.this.quantity > 0) {
                    ProductPageActivity.this.quantity = ProductPageActivity.this.quantity - 1;
                    ProductPageActivity.this.quantityTv.setText(ProductPageActivity.this.quantity + "");
                }
            }
        });
        this.shareButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                Product product = ProductPageActivity.productArrayList.get(ProductPageActivity.this.pager.getCurrentItem());
                String shareBody = "Get " + product.getProductName() + " for just ₹" + product.getPrice() + " per " + product.getUnit() + " with free home delivery anywhere in Jabalpur. Order Now!!!" + "\n\nDownload the app: " + "market://details?id=" + ProductPageActivity.this.getPackageName();
                sharingIntent.putExtra("android.intent.extra.SUBJECT", "Download VegetableShoppy");
                sharingIntent.putExtra("android.intent.extra.TEXT", shareBody);
                ProductPageActivity.this.startActivity(sharingIntent);
            }
        });
        this.cartButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductPageActivity.this.startActivity(new Intent(ProductPageActivity.this, CartActivity.class));
            }
        });
        this.favoriteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Product product = ProductPageActivity.productArrayList.get(ProductPageActivity.this.pager.getCurrentItem());
                if (!Data.isThisInWishList(product)) {
                    Data.addToWishList(product, ProductPageActivity.this);
                    Toast.makeText(ProductPageActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Data.removeFromWishList(product, ProductPageActivity.this);
                    Toast.makeText(ProductPageActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }
                ProductPageActivity.this.updateActionBarButtons();
            }
        });
        this.addToCart.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductPageActivity.this.quantity <= 0) {
                    Toast.makeText(ProductPageActivity.this, "Increase quantity", Toast.LENGTH_SHORT).show();
                } else if (Cart.addItems(new CartItem(ProductPageActivity.this.currentProduct, ProductPageActivity.this.quantity), ProductPageActivity.this.getApplicationContext())) {
                    Toast.makeText(ProductPageActivity.this, ProductPageActivity.this.quantity + " " + ProductPageActivity.this.currentProduct.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
                    ProductPageActivity.this.updateActionBarButtons();
                }
            }
        });
        updateActionBarButtons();
        sendDataToParse();
    }

    /* access modifiers changed from: private */
    public void updateAddToCartView() {
        if (this.currentProduct.isAvailInStocks()) {
            this.outOfStocks.setVisibility(View.GONE);
            this.addToCartView.setVisibility(View.VISIBLE);
            return;
        }
        this.outOfStocks.setVisibility(View.VISIBLE);
        this.addToCartView.setVisibility(View.GONE);
    }

    private void sendDataToParse() {
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        updateActionBarButtons();
        updateAddToCartView();
    }

    /* access modifiers changed from: private */
    public void updateActionBarButtons() {
        if (Data.isThisInWishList(this.currentProduct)) {
            this.favoriteButton.setImageResource(R.drawable.fav_red);
        } else {
            this.favoriteButton.setImageResource(R.drawable.fave_outline);
        }
        if (Cart.isInCart(this.currentProduct)) {
            this.cartButton.setImageResource(R.drawable.cart_filled);
        } else {
            this.cartButton.setImageResource(R.drawable.cart_outline);
        }
    }

    public void close(View view) {
        finish();
    }
}
