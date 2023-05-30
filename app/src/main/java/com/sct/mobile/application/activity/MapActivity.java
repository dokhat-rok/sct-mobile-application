package com.sct.mobile.application.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.sct.mobile.application.R;
import com.sct.mobile.application.component.observed.impl.ParkingObservedImpl;
import com.sct.mobile.application.component.observed.impl.PriceObservedImpl;
import com.sct.mobile.application.component.observed.impl.RentObservedImpl;
import com.sct.mobile.application.component.observed.impl.TransportObservedImpl;
import com.sct.mobile.application.component.observed.impl.TripObservedImpl;
import com.sct.mobile.application.component.subscriber.ParkingSubscriber;
import com.sct.mobile.application.component.subscriber.PriceSubscriber;
import com.sct.mobile.application.component.subscriber.RentSubscriber;
import com.sct.mobile.application.component.subscriber.TransportSubscriber;
import com.sct.mobile.application.component.subscriber.TripSubscriber;
import com.sct.mobile.application.dialog.FinishDialog;
import com.sct.mobile.application.fragment.CurrentTripFragment;
import com.sct.mobile.application.fragment.FilterFragment;
import com.sct.mobile.application.fragment.MenuFragment;
import com.sct.mobile.application.fragment.SelectedFragment;
import com.sct.mobile.application.mapper.RentMapper;
import com.sct.mobile.application.model.dto.ParkingDto;
import com.sct.mobile.application.model.dto.PriceDto;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.dto.TransportDto;
import com.sct.mobile.application.model.dto.TripBeginDto;
import com.sct.mobile.application.model.dto.TripEndDto;
import com.sct.mobile.application.model.enums.RentStatus;
import com.sct.mobile.application.model.enums.TransportType;
import com.sct.mobile.application.service.GeoStaticService;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Geo;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationManagerUtils;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MapActivity extends AppCompatActivity implements TransportSubscriber, ParkingSubscriber, TripSubscriber, PriceSubscriber, RentSubscriber {

    private MapView mapView;
    private MapObjectCollection mapObjects;

    private Toast toast;

    private FilterFragment filterFragment;
    private LinearLayout selectedLayout;
    private LinearLayout filterLayout;
    private LinearLayout menuLayout;
    private LinearLayout currentTripLayout;

    private static boolean isInitialize = false;
    private final float animTranslationYBy = 1500f;
    private final int animDuration = 1000;
    private final double DISTANCE_TO_TRANSPORT = 50d;

    private final List<PlacemarkMapObject> bicycleList = new ArrayList<>();
    private final List<PlacemarkMapObject> scooterList = new ArrayList<>();
    private final List<PlacemarkMapObject> parkingList = new ArrayList<>();
    private List<ParkingDto> parking;

    private final TransportObservedImpl transportObserved = new TransportObservedImpl();
    private final ParkingObservedImpl parkingObserved = new ParkingObservedImpl();
    private final TripObservedImpl tripObserved = new TripObservedImpl();
    private final PriceObservedImpl priceObserved = new PriceObservedImpl();
    private final RentObservedImpl rentObserved = new RentObservedImpl();

    private TransportDto selectedTransport;
    private MapObject selectedMapObject;
    private RentDto currentTripRent;

    private Timer getObjectsTimer;
    private Timer currentTripTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInitialize) {
            MapKitFactory.setApiKey("22a84de5-deb2-4b2e-883a-246299088e73");
            MapKitFactory.initialize(this);
            isInitialize = true;
        }
        this.checkRootLocation();
        this.setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapview);

        toast = new Toast(this);

        MapKit mapKit = MapKitFactory.getInstance();
        UserLocationLayer userLocation = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocation.setVisible(true);


        mapView.getMap().move(
                new CameraPosition(Objects.requireNonNull(LocationManagerUtils
                                .getLastKnownLocation()).getPosition(),
                        15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.0f),
                null);
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        transportObserved.subscribe(this);
        parkingObserved.subscribe(this);
        tripObserved.subscribe(this);
        priceObserved.subscribe(this);
        rentObserved.subscribe(this);

        this.getMapObjects();
        rentObserved.getAllRent(RentStatus.OPEN);

        getObjectsTimer = new Timer();
        getObjectsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getMapObjects();
            }
        }, 5000L, 5000L);
    }

    private void getMapObjects() {
        this.updateParking();
        this.updateBicycle();
        this.updateScooter();
    }

    public void updateParking() {
        if (!filterFragment.isParkingChecked()) this.deleteMapObjects(parkingList);
        parkingObserved.getAllParking();
    }

    public void updateBicycle() {
        if (filterFragment.isBicycleChecked())
            transportObserved.getAllTransport(TransportType.BICYCLE);
        else this.deleteMapObjects(bicycleList);
    }

    public void updateScooter() {
        if (filterFragment.isScooterChecked())
            transportObserved.getAllTransport(TransportType.SCOOTER);
        else this.deleteMapObjects(scooterList);
    }

    @Override
    public void acceptGetAllParking(List<ParkingDto> parkingList) {
        this.populateParking(parkingList);
    }

    @Override
    public void errorGetAllParking(String error) {
        this.notification(error);
    }

    @Override
    public void acceptGetAllTransport(TransportType type, List<TransportDto> transportList) {
        switch (type) {
            case BICYCLE -> this.populateBicycle(transportList);
            case SCOOTER -> this.populateScooter(transportList);
        }
    }

    @Override
    public void errorGetAllTransport(String error) {
        this.notification(error);
    }

    @Override
    public void acceptBeginTrip(RentDto rent) {
        currentTripRent = rent;
        mapObjects.remove(selectedMapObject);
        this.removeSelectLayout(true);
        this.initCurrentTripLayout();
        this.notification("Приятного пути!");
        this.startGeo();
    }

    @Override
    public void errorBeginTrip(String error) {
        this.notification(error);
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void acceptEndTrip(RentDto rent) {
        this.stopGeo();
        FinishDialog dialog = new FinishDialog(rent);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction();
        dialog.show(manager, "finish");

        this.currentTripTimer.cancel();
        currentTripLayout.removeAllViews();
        currentTripLayout = null;
        currentTripRent = null;
        this.getMapObjects();
    }

    @Override
    public void errorEndTrip(String error) {
        this.notification(error);
    }

    @Override
    public void acceptPrice(PriceDto price) {
        TextView beginCoast = selectedLayout.findViewById(R.id.select_begin_coast);
        TextView perMinuteCoast = selectedLayout.findViewById(R.id.select_per_minute_coast);
        String init = price.getInit() + " ₽";
        String minute = price.getPerMinute() + " ₽";
        beginCoast.setText(init);
        perMinuteCoast.setText(minute);
        this.findViewById(R.id.select_loading_panel).setVisibility(View.INVISIBLE);
        this.findViewById(R.id.select_price_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void errorPrice(String error) {
        this.notification(error);
    }

    @Override
    public void acceptGetAllRent(List<RentDto> rentList) {
        if (rentList.size() == 0) return;
        currentTripRent = rentList.get(0);
        this.startGeo();
        this.initCurrentTripLayout();
    }

    private void startGeo() {
        if(GeoStaticService.isStart()) return;
        GeoStaticService.setCurrentRent(currentTripRent);
        GeoStaticService.startSent();
    }

    private void stopGeo() {
        GeoStaticService.stopSent();
    }

    @Override
    public void errorGetAllRent(String error) {
        this.notification(error);
    }

    private void notification(String message) {
        toast.setText(message);
        toast.show();
    }

    private void initCurrentTripLayout() {
        currentTripLayout = this.findViewById(R.id.map_current_layout);
        CurrentTripFragment currentTrip = new CurrentTripFragment(this, currentTripLayout);
        currentTripLayout.addView(currentTrip);

        Button endButton = currentTripLayout.findViewById(R.id.current_end_button);
        endButton.setOnClickListener(this::onEndTripClick);
        endButton.setClickable(true);

        int image = currentTripRent.getTransport().getType() == TransportType.BICYCLE ?
                R.drawable.current_bicycle :
                R.drawable.current_scooter;
        ((ImageView) currentTripLayout.findViewById(R.id.current_transport_image))
                .setImageResource(image);
        TextView tName = currentTripLayout.findViewById(R.id.current_name_transport);
        tName.setText(RentMapper.dtoToView(currentTripRent).getTransport());

        currentTripTimer = new Timer();
        currentTripTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> fillCurrentTrip());
            }
        }, 0L, 1000L);
        currentTripLayout.setVisibility(View.VISIBLE);
    }

    private void fillCurrentTrip() {
        TextView time = currentTripLayout.findViewById(R.id.current_time);
        ZonedDateTime begin = ZonedDateTime.parse(currentTripRent.getBeginTimeRent());
        Duration delta = Duration.between(begin, ZonedDateTime.now());

        long seconds = delta.getSeconds();
        long hours = seconds / 3_600;
        seconds %= 3_600;
        long minutes = seconds / 60;
        seconds %= 60;

        String text = String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, seconds);
        time.setText(text);
    }

    private void populateParking(List<ParkingDto> parkingList) {
        parking = parkingList;
        if (!filterFragment.isParkingChecked()) return;
        this.deleteMapObjects(this.parkingList);
        parkingList.forEach(p -> this.populateMapObject(
                this.parkingList,
                p,
                p.getCoordinates(),
                R.drawable.parking));
    }

    private void populateScooter(List<TransportDto> transportList) {
        this.deleteMapObjects(this.scooterList);
        transportList.stream()
                .filter(this::notIsSelectedTransport)
                .forEach(t -> this.populateMapObject(
                        this.scooterList,
                        t,
                        t.getCoordinates(),
                        R.drawable.scooter));
    }

    private void populateBicycle(List<TransportDto> transportList) {
        this.deleteMapObjects(this.bicycleList);
        transportList.stream()
                .filter(this::notIsSelectedTransport)
                .forEach(t -> this.populateMapObject(
                        this.bicycleList,
                        t,
                        t.getCoordinates(),
                        R.drawable.bike));
    }

    private void deleteMapObjects(List<PlacemarkMapObject> objects) {
        objects.stream()
                .filter(this::notIsSelectedPlacemarkMapObject)
                .forEach(o -> {
                    try {
                        mapObjects.remove(o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        objects.clear();
    }

    private boolean notIsSelectedPlacemarkMapObject(PlacemarkMapObject o) {
        try {
            if (o.getUserData() instanceof TransportDto t) return this.notIsSelectedTransport(t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean notIsSelectedTransport(TransportDto t) {
        if (selectedTransport == null) return true;
        return !t.getId().equals(selectedTransport.getId());
    }

    private void populateMapObject(List<PlacemarkMapObject> mapObjects,
                                   Object model,
                                   String coordinates,
                                   int resourceId) {
        String[] c = coordinates.split(",");
        Point point = new Point(Double.parseDouble(c[0]), Double.parseDouble(c[1]));
        PlacemarkMapObject mapObject = this.mapObjects.addPlacemark(point);
        mapObject.setIcon(ImageProvider.fromResource(this, resourceId));
        mapObject.setVisible(true);
        mapObject.setUserData(model);
        mapObject.addTapListener(placeMarkTapListener);
        mapObjects.add(mapObject);
    }

    private final MapObjectTapListener placeMarkTapListener = (mapObject, point) -> {
        if (mapObject instanceof PlacemarkMapObject) {
            Object data = mapObject.getUserData();
            if (data instanceof ParkingDto) this.parkingEvent((ParkingDto) data);
            else if (data instanceof TransportDto) {
                selectedTransport = (TransportDto) mapObject.getUserData();
                this.selectedMapObject = mapObject;
                this.transportEvent();
            }
        }
        return true;
    };

    private void parkingEvent(ParkingDto parking) {
        this.cameraFocus(parking.getCoordinates(), 18.0f);
        notification("Парковка: " + parking.getName());
    }

    private void transportEvent() {
        this.initSelectedLayout(selectedTransport);
        priceObserved.getPrice(selectedTransport.getType());
        this.cameraFocus(selectedTransport.getCoordinates(), 18.0f);
    }

    private void initSelectedLayout(TransportDto transport) {
        if (selectedLayout == null) {
            selectedLayout = findViewById(R.id.map_selected_layout);
            SelectedFragment selectedFragment = new SelectedFragment(this, selectedLayout);
            menuLayout.addView(selectedFragment);
            selectedLayout.findViewById(R.id.select_begin_button)
                    .setOnClickListener(this::onBeginTripClick);
            selectedLayout.findViewById(R.id.select_remove_button)
                    .setOnClickListener(this::onRemoveSelectClick);
        }

        TextView transportId = selectedLayout.findViewById(R.id.select_transport_id);
        VideoView video = selectedLayout.findViewById(R.id.select_video);

        transportId.setText(transport.getIdentificationNumber());

        String path = "android.resource://" + getPackageName() + "/";
        path += transport.getType() == TransportType.BICYCLE ?
                R.raw.bicycle_animated :
                R.raw.scooter_animated;
        video.setVideoURI(Uri.parse(path));
        video.setOnPreparedListener(this::loopVideo);
        video.start();
    }

    private void loopVideo(MediaPlayer mp) {
        mp.setLooping(true);
    }

    private void cameraFocus(String coordinates, float zoom) {
        String[] c = coordinates.split(",");
        Point point = new Point(Double.parseDouble(c[0]), Double.parseDouble(c[1]));
        mapView.getMap().move(
                new CameraPosition(point, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.0f), null);
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

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        menuLayout = findViewById(R.id.map_menu);
        MenuFragment menuFragment = new MenuFragment(getApplicationContext(), menuLayout);
        menuLayout.addView(menuFragment);
        menuLayout.findViewById(R.id.menu_profile_button).setOnClickListener(this::onProfileCLick);
        menuLayout.findViewById(R.id.menu_rent_button).setOnClickListener(this::onRentClick);
        menuLayout.findViewById(R.id.menu_filter_button).setOnClickListener(this::onFilterClick);

        filterLayout = findViewById(R.id.map_menu_filter);
        filterFragment = new FilterFragment(getApplicationContext(), filterLayout, this);
        filterLayout.findViewById(R.id.filter_remove_button).setOnClickListener(this::onRemoveFilterClick);
        filterLayout.setTranslationY(animTranslationYBy);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        this.getObjectsTimer.cancel();
        if(currentTripTimer != null) this.currentTripTimer.cancel();
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

    @SuppressLint("DefaultLocale")
    public void onBeginTripClick(View view) {
        if (selectedTransport == null) return;
        if (currentTripRent != null) {
            notification("У вас уже есть активная поездка");
            return;
        }

        Location location = LocationManagerUtils.getLastKnownLocation();
        double distance = -1;
        if(location != null) {
            distance = Geo.distance(
                    location.getPosition(),
                    this.getPointFromString(selectedTransport.getCoordinates()));
        }
        if(distance == -1) {
            this.notification("Не удается определить местоположение");
            return;
        }
        else if(distance > DISTANCE_TO_TRANSPORT) {
            distance -= DISTANCE_TO_TRANSPORT;
            this.notification(String.format("Подойдите ближе на %5.0f %s",
                    distance, this.getMetreWord(distance)));
            return;
        }

        view.setClickable(false);
        tripObserved.beginTrip(TripBeginDto.builder()
                .parkingId(selectedTransport.getParking().getId())
                .transportId(selectedTransport.getId())
                .build());
    }

    public void onRemoveSelectClick(View view) {
        this.removeSelectLayout(false);
    }

    private void removeSelectLayout(boolean isStart) {
        this.cameraFocus(selectedTransport.getCoordinates(), 16.0f);
        if(!isStart) {
            if(selectedTransport.getType() == TransportType.BICYCLE)
                bicycleList.add((PlacemarkMapObject) selectedMapObject);
            else scooterList.add((PlacemarkMapObject) selectedMapObject);
        }
        selectedLayout.removeAllViews();
        selectedLayout = null;
        selectedTransport = null;
        selectedMapObject = null;
    }

    public void onRemoveFilterClick(View view) {
        filterLayout.animate().translationYBy(animTranslationYBy).setDuration(animDuration);
    }

    @SuppressLint("DefaultLocale")
    public void onEndTripClick(View view) {

        List<ParkingDto> parkingDistance = parking.stream()
                .sorted(Comparator.comparing(p -> Geo.distance(
                        this.getPointFromString(p.getCoordinates()),
                        GeoStaticService.getCurrentPoint())))
                .collect(Collectors.toList());

        ParkingDto nearP = parkingDistance.get(0);
        double distance = Geo.distance(this.getPointFromString(nearP.getCoordinates()),
                GeoStaticService.getCurrentPoint());

        if(distance > nearP.getAllowedRadius()) {
            this.notification(String
                    .format("До ближайшей парковки %6.0f %s", distance, this.getMetreWord(distance)));
            return;
        }

        notification("Завершаем поездку");
        currentTripLayout.findViewById(R.id.current_end_button).setClickable(false);
        tripObserved.endTrip(TripEndDto.builder()
                .parkingId(nearP.getId())
                .rentId(currentTripRent.getId())
                .transportId(currentTripRent.getTransport().getId())
                .build());
    }

    private Point getPointFromString(String c) {
        String[] coordinates = c.split(",");
        return  new Point(Double.parseDouble(coordinates[0]),
                Double.parseDouble(coordinates[1]));
    }

    private String getMetreWord(Double d) {
        return switch ((int) (Math.round(d) % 10)) {
            case 0, 5, 6, 7, 8, 9 -> "метров";
            case 1 -> "метр";
            case 2, 3, 4 -> "метра";
            default -> "";
        };
    }
}