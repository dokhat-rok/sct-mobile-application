package com.sct.mobile.application.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.sct.mobile.application.model.dto.RoutePointDto;
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
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RentActivity extends AppCompatActivity implements RentSubscriber {

    private final RentObservedImpl rentObserved = new RentObservedImpl();

    private Toast toast;

    private RelativeLayout routeLayout;
    private MapView mapView;
    private List<RentDto> rents;

    private MapObjectCollection mapObjects;

    private TextView distance;
    private TextView time;
    private TextView speed;
    private TextView start;
    private TextView finish;
    private TextView money;
    private TextView maxSpeed;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.checkRootLocation();
        this.setContentView(R.layout.activity_rent);
        this.findViewById(R.id.rent_remove_button).setOnClickListener(this::onRemoveClick);
        toast = new Toast(this);

        distance = this.findViewById(R.id.route_distance);
        time = this.findViewById(R.id.route_time);
        speed = this.findViewById(R.id.route_speed);
        start = this.findViewById(R.id.route_start);
        finish = this.findViewById(R.id.route_finish);
        money = this.findViewById(R.id.route_money);
        maxSpeed = this.findViewById(R.id.route_max_speed);
        name = this.findViewById(R.id.route_name);

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
        List<Point> polylinePoints = selected.getRoutePoints().stream()
                .map(p -> new Point(p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());
        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints));
        polyline.setStrokeColor(Color.parseColor("#E73875"));

        this.initStartAndFinish(polylinePoints);
        this.fillStats(rent, selected, polylinePoints);

        mapView.getMap().move(
                new CameraPosition(this.getCenter(polylinePoints), this.getZoom(polylinePoints),
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.5f),
                null);
    }

    @SuppressLint("DefaultLocale")
    private void fillStats(RentView rent, RentDto selected, List<Point> polylinePoints) {
        distance.setText(rent.getDistance());
        time.setText(rent.getTime());
        money.setText(rent.getAmount());
        start.setText(selected.getBeginParking().getName());
        finish.setText(selected.getEndParking().getName());
        name.setText(rent.getTransport());

        List<RoutePointDto> points = selected.getRoutePoints();
        double speed = 0;
        double max = 0;
        double count = 0;
        for(int i = 0; i < polylinePoints.size() - 2; i++) {
            double d = Geo.distance(polylinePoints.get(i), polylinePoints.get(i + 1));
            ZonedDateTime t1 = ZonedDateTime.parse(points.get(0).getCreatedDate());
            ZonedDateTime t2 = ZonedDateTime.parse(points.get(i + 1).getCreatedDate());
            Duration delta = Duration.between(t1, t2);
            double s = d / delta.toMillis();
            if(s >= max) {
                speed += s;
                count ++;
            }
            if(s > max) max = s;
        }

        speed /= count;
        speed *= 3_600;
        max *= 3_600;

        this.speed.setText(String.format("%3.0f км/ч", speed));
        this.maxSpeed.setText(String.format("%3.0f км/ч", max));
    }

    /*private Point getCenter(List<Point> polylinePoints) {
        return polylinePoints.get(polylinePoints.size() / 2);
    }*/

    private Point getCenter(List<Point> polylinePoints) {
        Point p1 = polylinePoints.get(0);
        Point p2 = polylinePoints.get(polylinePoints.size() - 1);
        return new Point(
                (p1.getLatitude() + p2.getLatitude()) / 2,
                (p1.getLongitude() + p2.getLongitude()) / 2
        );
    }

    private void initStartAndFinish(List<Point> polylinePoints) {
        this.createMapObject(polylinePoints.get(0), R.drawable.location_a);
        this.createMapObject(polylinePoints.get(polylinePoints.size() - 1), R.drawable.location_b);
    }

    private void createMapObject(Point point, int r) {
        PlacemarkMapObject mapObject = this.mapObjects.addPlacemark(point);
        mapObject.setIcon(ImageProvider.fromResource(this, r));
        mapObject.setVisible(true);
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