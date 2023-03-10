package com.sct.mobile.application.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.sct.mobile.application.R;
import com.sct.mobile.application.view.FilterView;
import com.sct.mobile.application.view.MenuView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.LinearRing;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polygon;
import com.yandex.mapkit.location.LocationSimulator;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectDragListener;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolygonMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.AnimatedImageProvider;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Handler animationHandler;

    private Toast toast;

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

        if (!isInitialize) {
            MapKitFactory.setApiKey("22a84de5-deb2-4b2e-883a-246299088e73");
            MapKitFactory.initialize(this);
            isInitialize = true;
        }
        this.checkRootLocation();
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapview);

        toast = new Toast(this);

        MapKit mapKit = MapKitFactory.getInstance();
        UserLocationLayer userLocation = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocation.setVisible(true);

        mapView.getMap().move(
                new CameraPosition(new Point(47.228713, 39.715841), 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.0f),
                null);
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        animationHandler = new Handler();
        this.createMapObjects();
    }


    private void createMapObjects(){
        Point point1 = new Point(47.228713, 39.715841);
        PlacemarkMapObject scooter = mapObjects.addPlacemark(point1);
        scooter.setIcon(ImageProvider.fromResource(this, R.drawable.scooter));
        scooter.setVisible(true);
        scooter.setUserData("Самокат");
        scooter.addTapListener(placeMarkTapListener);

        Point point2 = new Point(47.228713, 39.715341);
        PlacemarkMapObject bike = mapObjects.addPlacemark(point2);
        bike.setIcon(ImageProvider.fromResource(this, R.drawable.bike));
        bike.setVisible(true);
        bike.setUserData("Велосипед");
        bike.addTapListener(placeMarkTapListener);

        Point point3 = new Point(47.228713, 39.714841);
        PlacemarkMapObject parking = mapObjects.addPlacemark(point3);
        parking.setIcon(ImageProvider.fromResource(this, R.drawable.parking));
        parking.setVisible(true);
        parking.setUserData("Парковка");
        parking.addTapListener(placeMarkTapListener);
    }

    private final MapObjectTapListener placeMarkTapListener = (mapObject, point) -> {
        if(mapObject instanceof PlacemarkMapObject){
            String data = (String) mapObject.getUserData();
            toast.setText(data);
            toast.show();
        }
        return true;
    };

    private void checkRootLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
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

    public void onProfileCLick(View view) {
        this.startActivity(new Intent(MapActivity.this, ProfileActivity.class));
        this.finish();
    }

    public void onRentClick(View view) {
        this.startActivity(new Intent(MapActivity.this, RentActivity.class));
        this.finish();
    }

    public void onFilterClick(View view) {
        filterLayout.animate().translationYBy(-animTranslationYBy).setDuration(animDuration);
        if (filterLayout.getTranslationY() < animTranslationYBy)
            filterLayout.animate()
                    .translationYBy(animTranslationYBy - filterLayout.getTranslationY())
                    .setDuration(animDuration);
    }

    public void onRemoveFilterClick(View view) {
        filterLayout.animate().translationYBy(animTranslationYBy).setDuration(animDuration);
    }
}