package com.sct.mobile.application.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.sct.mobile.application.R;
import com.sct.mobile.application.activity.MapActivity;

@SuppressLint({"ViewConstructor", "UseSwitchCompatOrMaterialCode"})
public class FilterFragment extends LinearLayout {

    private Switch parkingSwitch;

    private Switch scooterSwitch;

    private Switch bicycleSwitch;

    private final MapActivity mapActivity;

    public FilterFragment(Context context, LinearLayout parent, MapActivity mapActivity) {
        super(context);
        this.mapActivity = mapActivity;
        initView(context, parent);
    }

    @SuppressLint("MissingInflatedId")
    public void initView(final Context context, LinearLayout parent) {
        View filter = LayoutInflater.from(context).inflate(R.layout.fragment_filter_menu, parent, true);

        parkingSwitch = filter.findViewById(R.id.filter_parking_switch);
        bicycleSwitch = filter.findViewById(R.id.filter_bicycle_switch);
        scooterSwitch = filter.findViewById(R.id.filter_scooter_switch);

        parkingSwitch.setOnClickListener(this::parkingClick);
        bicycleSwitch.setOnClickListener(this::bicycleClick);
        scooterSwitch.setOnClickListener(this::scooterClick);
    }

    public boolean isParkingChecked() {
        return parkingSwitch.isChecked();
    }

    public boolean isScooterChecked() {
        return scooterSwitch.isChecked();
    }

    public boolean isBicycleChecked() {
        return bicycleSwitch.isChecked();
    }

    private void parkingClick(View view) {
        mapActivity.updateParking();
    }

    private void bicycleClick(View view) {
        mapActivity.updateBicycle();
    }

    private void scooterClick(View view) {
        mapActivity.updateScooter();
    }
}
