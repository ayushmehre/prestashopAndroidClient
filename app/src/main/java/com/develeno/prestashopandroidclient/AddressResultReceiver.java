package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class AddressResultReceiver extends ResultReceiver {
    private boolean isSuccess;
    private String message;
    private final OnDoneListener onDoneListener;

    public AddressResultReceiver(Handler handler, OnDoneListener onDoneListener2) {
        super(handler);
        this.onDoneListener = onDoneListener2;
    }

    /* access modifiers changed from: protected */
    public void onReceiveResult(int resultCode, Bundle resultData) {
        this.isSuccess = resultCode == 0;
        this.message = resultData.getString(Constants.RESULT_DATA_KEY);
        Log.d("REC", "GOT DATA: " + this.message);
        this.onDoneListener.done();
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}
