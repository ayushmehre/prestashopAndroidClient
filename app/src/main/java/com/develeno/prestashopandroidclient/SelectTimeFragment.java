package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import androidx.fragment.app.Fragment;

public class SelectTimeFragment extends Fragment {
    public static final String TIME_SLOT_1 = "Time Slot 1";
    public static final String TIME_SLOT_2 = "Time Slot 2";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_select_time, container, false);
        NumberPicker picker = rootView.findViewById(R.id.numberPicker);
        final String[] displayedValues = {TIME_SLOT_1, TIME_SLOT_2};
        picker.setMinValue(0);
        picker.setOnLongClickListener(null);
        picker.setMaxValue(displayedValues.length - 1);
        picker.setDisplayedValues(displayedValues);
        picker.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Data.getCurrentOrder().setTimeslot(displayedValues[newVal]);
            }
        });
        Data.getCurrentOrder().setTimeslot(displayedValues[picker.getValue()]);
        return rootView;
    }
}
