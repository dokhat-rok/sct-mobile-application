package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sct.mobile.application.R;
import com.sct.mobile.application.view.MenuView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MenuView menuView;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("22a84de5-deb2-4b2e-883a-246299088e73");
        MapKitFactory.initialize(this);

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
        layout = findViewById(R.id.map_menu);
        menuView = new MenuView(getApplicationContext(),layout);
        layout.addView(menuView);
        layout.findViewById(R.id.menu_profile_button).setOnClickListener(this::onProfileCLick);
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
}