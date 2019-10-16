package com.develeno.prestashopandroidclient;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "INTENT_TAG";
    private ResultReceiver mReceiver;

    public FetchAddressIntentService(String name) {
        super(name);
    }

    public FetchAddressIntentService() {
        super("nbnb");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        String errorMessage = "";
        this.mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        List<Address> addresses = null;
        try {
            addresses = new Geocoder(this, Locale.getDefault()).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            errorMessage = "Service not available";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "invalid latitude and longitude used";
            Log.e(TAG, errorMessage + ". " + "Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No addresses Found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(1, errorMessage);
            return;
        }
        Address address = addresses.get(0);
        ArrayList<String> addressFragments = new ArrayList<>();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }
        Log.i(TAG, "Address Found");
        deliverResultToReceiver(0, TextUtils.join(System.getProperty("line.separator"), addressFragments));
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        this.mReceiver.send(resultCode, bundle);
    }
}
