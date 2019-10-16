package com.develeno.prestashopandroidclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SplashScreenPagerAdapter extends FragmentStatePagerAdapter {
    public SplashScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentSplashScreen fragmentSplashScreen = new FragmentSplashScreen();
                Bundle bundle = new Bundle();
                bundle.putInt("selection", 1);
                fragmentSplashScreen.setArguments(bundle);
                return fragmentSplashScreen;
            case 1:
                FragmentSplashScreen fragmentSplashScreen2 = new FragmentSplashScreen();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("selection", 2);
                fragmentSplashScreen2.setArguments(bundle2);
                return fragmentSplashScreen2;
            case 2:
                FragmentSplashScreen fragmentSplashScreen3 = new FragmentSplashScreen();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("selection", 3);
                fragmentSplashScreen3.setArguments(bundle3);
                return fragmentSplashScreen3;
            case 3:
                return new FragmentGetStarted();
            default:
                return new FragmentGetStarted();
        }
    }

    public int getCount() {
        return 4;
    }
}
