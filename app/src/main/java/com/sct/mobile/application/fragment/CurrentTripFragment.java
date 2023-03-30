package com.sct.mobile.application.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sct.mobile.application.R;

@SuppressLint("ViewConstructor")
public class CurrentTripFragment extends LinearLayout {

    public CurrentTripFragment(Context context, LinearLayout parent) {
        super(context);
        this.initFragment(context, parent);
    }

    private void initFragment(Context context, LinearLayout parent) {
        LayoutInflater.from(context).inflate(R.layout.fragment_current_trip, parent, true);
    }
}
