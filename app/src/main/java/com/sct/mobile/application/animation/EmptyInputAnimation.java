package com.sct.mobile.application.animation;

import android.graphics.Color;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class EmptyInputAnimation {

    public static void shake(View view){
        view.setBackgroundColor(Color.parseColor("#fa4b64"));
        TranslateAnimation shake = new TranslateAnimation(0, 15, 0, 5);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(6));
        view.startAnimation(shake);
    }
}
