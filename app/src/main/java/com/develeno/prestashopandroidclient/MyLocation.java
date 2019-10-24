//package com.develeno.prestashopandroidclient;
//
//import android.content.Intent;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.GoogleApiClient.Builder;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import java.util.Calendar;
//import java.util.Date;
//
//public class MyLocation {
//    private static MyLocation instance;
//    /* access modifiers changed from: private */
//    public AppCompatActivity activity = null;
//    private GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;
//    /* access modifiers changed from: private */
//    public Date mLastUpdateTime;
//    private LocationListener mLocationListener;
//    /* access modifiers changed from: private */
//    public AddressResultReceiver mResultReceiver;
//    private String myLocationName;
//
//    private MyLocation(AppCompatActivity activity2, OnDoneListener listener) {
//        this.activity = activity2;
//        Toast.makeText(activity2, "Building Client", Toast.LENGTH_SHORT).show();
//        buildGoogleApiClient(listener);
//        this.mGoogleApiClient.connect();
//    }
//
//    private MyLocation() {
//    }
//
//    public static MyLocation getInstance(AppCompatActivity activity2, OnDoneListener listener) {
//        if (instance != null) {
//            return instance;
//        }
//        instance = new MyLocation(activity2, listener);
//        return instance;
//    }
//
//    /* access modifiers changed from: protected */
//    public void startIntentService(final OnBackgroundTaskCompleted task) {
//        if (this.mGoogleApiClient.isConnected() && this.mLastLocation != null) {
//            this.mResultReceiver = new AddressResultReceiver(new Handler(), new OnDoneListener() {
//                public void done() {
//                    task.getResult(MyLocation.this.mResultReceiver.getMessage());
//                }
//            });
//            Intent intent = new Intent(this.activity, FetchAddressIntentService.class);
//            intent.putExtra(Constants.RECEIVER, this.mResultReceiver);
//            intent.putExtra(Constants.LOCATION_DATA_EXTRA, this.mLastLocation);
//            this.activity.startService(intent);
//            Toast.makeText(this.activity, "Started Intent Service", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public Location getLastLocation() {
//        if (!this.mGoogleApiClient.isConnected()) {
//            return null;
//        }
//        this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
//        if (this.mLastLocation != null) {
//            this.mLastUpdateTime = Calendar.getInstance().getTime();
//            return this.mLastLocation;
//        }
//       // Toast.makeText(this.activity, AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED, 0).show();
//        return null;
//    }
//
//    public void stopLocationUpdates() {
//        if (this.mGoogleApiClient.isConnected() && this.mLocationListener != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, (com.google.android.gms.location.LocationListener) this.mLocationListener);
//        }
//    }
//
//    public Date getmLastUpdateTime() {
//        return this.mLastUpdateTime;
//    }
//
//    @NonNull
//    private LocationListener getLocationListener() {
//        return new LocationListener() {
//            public void onLocationChanged(Location location) {
//                MyLocation.this.mLastUpdateTime = Calendar.getInstance().getTime();
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//    }
//
//    @NonNull
//    private OnConnectionFailedListener getOnConnectionFailedListener() {
//        return new OnConnectionFailedListener() {
//            public void onConnectionFailed(ConnectionResult connectionResult) {
//                Toast.makeText(MyLocation.this.activity, "Connection Failed", Toast.LENGTH_SHORT).show();
//            }
//        };
//    }
//
//    public void startLocationUpdates() {
//        if (this.mGoogleApiClient.isConnected()) {
//            this.mLocationListener = getLocationListener();
//            LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, createLocationRequest(), (com.google.android.gms.location.LocationListener) this.mLocationListener);
//        }
//    }
//
//    /* access modifiers changed from: protected */
//    public LocationRequest createLocationRequest() {
//        /*LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(100);
//        return mLocationRequest;*/
//        return null;
//    }
//
//    /* access modifiers changed from: protected */
//    public synchronized void buildGoogleApiClient(OnDoneListener listener) {
//        ConnectionCallbacks connectionCallbacks = getConnectionCallbacks(listener);
//        this.mGoogleApiClient = new Builder(this.activity).addConnectionCallbacks(connectionCallbacks).addOnConnectionFailedListener(getOnConnectionFailedListener()).addApi(LocationServices.API).addApi(Places.PLACE_DETECTION_API).addApi(Places.GEO_DATA_API).build();
//    }
//
//    @NonNull
//    private ConnectionCallbacks getConnectionCallbacks(final OnDoneListener listener) {
//        return new ConnectionCallbacks() {
//            public void onConnected(Bundle bundle) {
//                Toast.makeText(MyLocation.this.activity, "Connected", Toast.LENGTH_SHORT).show();
//                if (listener != null) {
//                    listener.done();
//                }
//            }
//
//            public void onConnectionSuspended(int i) {
//            }
//        };
//    }
//
//    public GoogleApiClient getGoogleApiClient() {
//        return this.mGoogleApiClient;
//    }
//
//    public String getMyLocationName() {
//        return this.myLocationName;
//    }
//}
