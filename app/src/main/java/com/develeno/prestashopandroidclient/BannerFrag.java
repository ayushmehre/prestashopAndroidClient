package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BannerFrag extends Fragment {
    private FragmentHome.SliderOffer offer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_banner, container, false);
        TextView title = rootView.findViewById(R.id.title);
        TextView heading = rootView.findViewById(R.id.heading);
        TextView desc = rootView.findViewById(R.id.desc);
        if (!(this.offer == null || title == null || heading == null || desc == null)) {
            title.setText(this.offer.getTitle().toUpperCase());
            heading.setText(this.offer.getHeading());
            desc.setText(this.offer.getDesc());
        }
        return rootView;
    }

    public void setOffer(FragmentHome.SliderOffer offer2) {
        this.offer = offer2;
    }
}
