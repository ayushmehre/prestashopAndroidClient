package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.view.View.OnClickListener;

public class MoreListItem {
    private String desc;
    private int imageId;
    private Intent intent;
    private OnClickListener onClickListener;
    private String title;

    public MoreListItem(String title2, String desc2, int imageId2, Intent intent2) {
        this.desc = desc2;
        this.title = title2;
        this.intent = intent2;
        this.imageId = imageId2;
    }

    public MoreListItem(String title2, String desc2, int imageId2, OnClickListener onClickListener2) {
        this.desc = desc2;
        this.title = title2;
        this.onClickListener = onClickListener2;
        this.imageId = imageId2;
    }

    public int getImageId() {
        return this.imageId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public OnClickListener getOnClickListener() {
        return this.onClickListener;
    }
}
