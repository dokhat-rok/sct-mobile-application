package com.sct.mobile.application.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sct.mobile.application.R;
import com.sct.mobile.application.adapter.RentAdapter;
import com.sct.mobile.application.component.observed.impl.RentObservedImpl;
import com.sct.mobile.application.component.subscriber.RentSubscriber;
import com.sct.mobile.application.mapper.RentMapper;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.enums.RentStatus;
import com.sct.mobile.application.model.view.RentView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Geo;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;

import java.util.List;
import java.util.stream.Collectors;

public class RentActivity extends AppCompatActivity implements RentSubscriber {

    private final RentObservedImpl rentObserved = new RentObservedImpl();

    private Toast toast;

    private RelativeLayout routeLayout;
    private MapView mapView;
    private List<RentDto> rents;

    private MapObjectCollection mapObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.checkRootLocation();
        this.setContentView(R.layout.activity_rent);
        this.findViewById(R.id.rent_remove_button).setOnClickListener(this::onRemoveClick);
        toast = new Toast(this);

        routeLayout = this.findViewById(R.id.rent_routes);
        routeLayout.setVisibility(View.INVISIBLE);
        this.findViewById(R.id.route_remove_button).setOnClickListener(this::onClickRemoveRoute);
        mapView = this.findViewById(R.id.route_mapview);
        MapKit mapKit = MapKitFactory.getInstance();
        UserLocationLayer userLocation = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocation.setVisible(true);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        rentObserved.subscribe(this);
        rentObserved.getAllRent(RentStatus.CLOSE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    public void onRemoveClick(View view) {
        this.startActivity(new Intent(RentActivity.this, MapActivity.class));
        this.finish();
    }

    @Override
    public void acceptGetAllRent(List<RentDto> rentList) {
        this.findViewById(R.id.rent_loadingPanel).setVisibility(View.INVISIBLE);
        this.rents = rentList;
        RecyclerView recyclerView = this.findViewById(R.id.rent_list);
        RentAdapter adapter = new RentAdapter(this, this.fillData(rentList));
        adapter.setRentActivity(this);
        recyclerView.setAdapter(adapter);
    }

    public void showRoute(RentView rent) {
        RentDto selected = rents.parallelStream()
                .filter(r -> r.getId().equals(rent.getId()))
                .findFirst().orElse(null);

        if(selected == null) {
            this.notification("Поездка не найдена");
            return;
        }
        if (selected.getRoutePoints().size() == 0) {
            this.notification("Нет геометок");
            return;
        }

        routeLayout.setVisibility(View.VISIBLE);
        List<Point> polylinePoints = selected.getRoutePoints().parallelStream()
                .map(p -> new Point(p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());
        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints));
        polyline.setStrokeColor(Color.parseColor("#E73875"));

        mapView.getMap().move(
                new CameraPosition(this.getCenter(polylinePoints), this.getZoom(polylinePoints),
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.5f),
                null);
    }

    private Point getCenter(List<Point> polylinePoints) {
        return polylinePoints.get(polylinePoints.size() / 2);
    }

    private float getZoom(List<Point> polylinePoints) {
        float d = (float) Geo.distance(polylinePoints.get(0),
                polylinePoints.get(polylinePoints.size() - 1));
        float zoom = 20;
        while (d > 1) {
            zoom -= 1;
            d /= 5;
        }
        zoom -= d;
        return zoom;
    }

    public void onClickRemoveRoute(View view) {
        routeLayout.setVisibility(View.INVISIBLE);
        mapObjects.clear();
    }

    @Override
    public void errorGetAllRent(String error) {
        this.notification(error);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                rentObserved.getAllRent(RentStatus.OPEN);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void notification(String text) {
        toast.setText(text);
        toast.show();
    }

    private List<RentView> fillData(List<RentDto> rentList) {
        return rentList.parallelStream()
                .map(RentMapper::dtoToView)
                .collect(Collectors.toList());
    }

    private void checkRootLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }
}