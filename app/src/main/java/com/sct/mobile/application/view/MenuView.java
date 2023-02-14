package com.sct.mobile.application.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sct.mobile.application.R;

@SuppressLint("ViewConstructor")
public class MenuView extends LinearLayout {


    public MenuView(Context context, LinearLayout parent) {
        super(context);
        initView(context, parent);
    }

    public void initView(final Context context,LinearLayout parent){
        View view= LayoutInflater.from(context).inflate(R.layout.view_menu,parent,true);
    }
}
