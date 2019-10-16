package com.develeno.prestashopandroidclient;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class BannerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<FragmentHome.SliderOffer> offers;

    public BannerAdapter(FragmentManager fm, ArrayList<FragmentHome.SliderOffer> offers2) {
        super(fm);
        this.offers = offers2;
    }

    public Fragment getItem(int position) {
        BannerFrag bannerFrag = new BannerFrag();
        bannerFrag.setOffer((FragmentHome.SliderOffer) this.offers.get(position));
        return bannerFrag;
    }

    public int getCount() {
        return this.offers.size();
    }
}
