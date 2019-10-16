package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentSplashScreen extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (getArguments().getInt("selection")) {
            case 1:
                return inflater.inflate(R.layout.splash_1, container, false);
            case 2:
                return inflater.inflate(R.layout.splash_2, container, false);
            case 3:
                return inflater.inflate(R.layout.splash_3, container, false);
            case 4:
                return inflater.inflate(R.layout.splash_4, container, false);
            default:
                return inflater.inflate(R.layout.splash_1, container, false);
        }
    }
}
