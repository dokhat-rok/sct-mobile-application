package com.sct.mobile.application.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sct.mobile.application.R;

import lombok.Getter;

@SuppressLint("ViewConstructor")
@Getter
public class RentView extends LinearLayout {

    private final int id;

    public RentView(Context context, LinearLayout parent, int id) {
        super(context);
        this.id = id;
        this.initView(context, parent);
    }

    private void initView(Context context, LinearLayout parent) {
        LayoutInflater.from(context).inflate(R.layout.view_rent, parent, true);
    }
}
