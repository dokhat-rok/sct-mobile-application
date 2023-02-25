package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sct.mobile.application.R;
import com.sct.mobile.application.view.FilterView;
import com.sct.mobile.application.view.MenuView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MenuView menuView;
    private FilterView filterView;
    private LinearLayout menuLayout;
    private LinearLayout filterLayout;
    private static boolean isInitialize = false;
    private final float animTranslationYBy = 700f;
    private final int animDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isInitialize){
            MapKitFactory.setApiKey("22a84de5-deb2-4b2e-883a-246299088e73");
            MapKitFactory.initialize(this);
            isInitialize = true;
        }

        setContentView(R.layout.activity_map);
//        this.findViewById(R.id.map_menu_button).setOnClickListener(this::setupMenu);
        mapView = findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(new Point(47.228713, 39.715841), 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
//        Point mappoint = new Point(55.79, 37.57);
//        mapView.getMap().getMapObjects().addPlacemark(mappoint);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        menuLayout = findViewById(R.id.map_menu);
        menuView = new MenuView(getApplicationContext(), menuLayout);
        menuLayout.addView(menuView);
        menuLayout.findViewById(R.id.menu_profile_button).setOnClickListener(this::onProfileCLick);
        menuLayout.findViewById(R.id.menu_rent_button).setOnClickListener(this::onRentClick);
        menuLayout.findViewById(R.id.menu_filter_button).setOnClickListener(this::onFilterClick);

        filterLayout = findViewById(R.id.map_menu_filter);
        filterView = new FilterView(getApplicationContext(), filterLayout);
        filterLayout.findViewById(R.id.filter_remove_button).setOnClickListener(this::onRemoveFilterClick);
        filterLayout.setTranslationY(animTranslationYBy);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    public void onProfileCLick(View view){
        this.startActivity(new Intent(MapActivity.this, ProfileActivity.class));
    }

    public void onRentClick(View view){
        this.startActivity(new Intent(MapActivity.this, RentActivity.class));
    }

    public void onFilterClick(View view){
        filterLayout.animate().translationYBy(-animTranslationYBy).setDuration(animDuration);

        if(filterLayout.getTranslationY() < animTranslationYBy)
            filterLayout.animate()
                    .translationYBy(animTranslationYBy - filterLayout.getTranslationY())
                    .setDuration(animDuration);
    }

    public void onRemoveFilterClick(View view){
        filterLayout.animate().translationYBy(animTranslationYBy).setDuration(animDuration);
    }
}