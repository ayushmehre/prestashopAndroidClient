package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccessFullActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_full);
    }

    public void continueShopping(View view) {
        finish();
    }

    public void checkOrderStatus(View view) {
        startActivity(new Intent(this, OrderHistoryActivty.class));
    }
}
