package com.develeno.prestashopandroidclient;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class Font {
    public static void applyFont(TextView textView, Context context) {
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "proxima-nova-soft-bold.ttf"));
    }
}
