package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CheckoutActivity extends AppCompatActivity {
    public static boolean isAddressSelected;
//    private CallbackManager callbackManager;
    /* access modifiers changed from: private */
//    public MyLocation myLocation;
    /* access modifiers changed from: private */
    public ViewPager pager;

    private class CheckoutPagerAdapter extends FragmentStatePagerAdapter {
        public CheckoutPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BillFragment();
                case 1:
                    Fragment fragment = new AddressFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("selectable", true);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    return new SelectTimeFragment();
                case 3:
                    return new OrderOverView();
                default:
                    return new BillFragment();
            }
        }

        public int getCount() {
            return 4;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        /*this.myLocation = MyLocation.getInstance(this, new OnDoneListener() {
            public void done() {
                CheckoutActivity.this.myLocation.startLocationUpdates();
                Location location = CheckoutActivity.this.myLocation.getLastLocation();
                if (location != null) {
                    Toast.makeText(CheckoutActivity.this, location.getLatitude() + "", 0).show();
                }
            }
        });*/
//        FacebookSdk.sdkInitialize(this);
//        this.callbackManager = Factory.create();
        final TextView back = findViewById(R.id.back);
        TextView next = findViewById(R.id.next);
        final View view = findViewById(R.id.relativeLayout2);
        final ImageView flow = findViewById(R.id.flow);
        this.pager = findViewById(R.id.pager);
        this.pager.setAdapter(new CheckoutPagerAdapter(getSupportFragmentManager()));
        this.pager.setOffscreenPageLimit(10);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CheckoutActivity.this.finish();
            }
        });
        next.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CheckoutActivity.this.pager.setCurrentItem(CheckoutActivity.this.pager.getCurrentItem() + 1);
            }
        });
        this.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 0) {
                    back.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            CheckoutActivity.this.finish();
                        }
                    });
                } else {
                    back.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            CheckoutActivity.this.pager.setCurrentItem(CheckoutActivity.this.pager.getCurrentItem() - 1);
                        }
                    });
                }
                if (position == 3) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                switch (position) {
                    case 0:
                        flow.setImageResource(R.drawable.flow);
                        return;
                    case 1:
                        flow.setImageResource(R.drawable.flow2);
                        return;
                    case 2:
                        flow.setImageResource(R.drawable.flow3);
                        return;
                    case 3:
                        flow.setImageResource(R.drawable.flow4);
                        return;
                    default:
                        return;
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onPause() {
//        if (this.myLocation != null) {
//            this.myLocation.stopLocationUpdates();
//        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    public CallbackManager getCallbackManager() {
//        return this.callbackManager;
//    }

    public ViewPager getViewPager() {
        return this.pager;
    }

    public void add(View view) {
    }
}
