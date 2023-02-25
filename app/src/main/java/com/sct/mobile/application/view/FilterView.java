package com.sct.mobile.application.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sct.mobile.application.R;

@SuppressLint("ViewConstructor")
public class FilterView extends LinearLayout {

    public FilterView(Context context, LinearLayout parent) {
        super(context);
        initView(context, parent);
    }

    @SuppressLint("MissingInflatedId")
    public void initView(final Context context, LinearLayout parent){
        LayoutInflater.from(context).inflate(R.layout.view_filter_menu,parent,true);
    }
}
