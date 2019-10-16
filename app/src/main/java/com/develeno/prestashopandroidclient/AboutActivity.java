package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_text);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void insta(View view) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/VegitableBazaar/")));
    }

    public void twitter(View view) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.twitter.com/VegitableBazaar/")));
    }

    /* renamed from: fb */
    public void mo6000fb(View view) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/VegitableBazaar/")));
    }
}
