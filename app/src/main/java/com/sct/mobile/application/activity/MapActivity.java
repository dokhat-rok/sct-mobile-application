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
import com.sct.mobile.application.dialog.EndTripDialog;
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
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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

    private final List<PlacemarkMapObject> bicycleList = new ArrayList<>();
    private final List<PlacemarkMapObject> scooterList = new ArrayList<>();
    private final List<PlacemarkMapObject> parkingList = new ArrayList<>();

    private final TransportObservedImpl transportObserved = new TransportObservedImpl();
    private final ParkingObservedImpl parkingObserved = new ParkingObservedImpl();
    private final TripObservedImpl tripObserved = new TripObservedImpl();
    private final PriceObservedImpl priceObserved = new PriceObservedImpl();
    private final RentObservedImpl rentObserved = new RentObservedImpl();

    private TransportDto selectedTransport;
    private RentDto currentTripRent;

    private Timer getObjectsTimer;
    private Timer currentTripTimer;
    private boolean makeEnd;

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
                new CameraPosition(new Point(47.228713, 39.715841), 15.0f, 0.0f, 0.0f),
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
        if (filterFragment.isParkingChecked()) parkingObserved.getAllParking();
        else this.deleteMapObjects(parkingList);
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
        this.removeSelectLayout();
        this.initCurrentTripLayout();
        this.notification("Приятного пути!");
    }

    @Override
    public void errorBeginTrip(String error) {
        this.notification(error);
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void acceptEndTrip(RentDto rent) {
        FinishDialog dialog = new FinishDialog(rent);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction();
        dialog.show(manager, "finish");

        this.makeEnd = false;
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
        this.initCurrentTripLayout();
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
                .forEach(o -> mapObjects.remove(o));
        objects.clear();
    }

    private boolean notIsSelectedPlacemarkMapObject(PlacemarkMapObject o) {
        if (o.getUserData() instanceof TransportDto t) return this.notIsSelectedTransport(t);
        return true;
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
                this.transportEvent();
            }
        }
        return true;
    };

    private void parkingEvent(ParkingDto parking) {
        if (!makeEnd) {
            this.cameraFocus(parking.getCoordinates());
            notification("Парковка: " + parking.getName());
            return;
        }
        notification("Завершаем поездку");
        currentTripLayout.findViewById(R.id.current_end_button).setClickable(false);
        tripObserved.endTrip(TripEndDto.builder()
                .parkingId(parking.getId())
                .rentId(currentTripRent.getId())
                .transportId(currentTripRent.getTransport().getId())
                .build());
    }

    private void transportEvent() {
        this.initSelectedLayout(selectedTransport);
        priceObserved.getPrice(selectedTransport.getType());
        this.cameraFocus(selectedTransport.getCoordinates());
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

    private void cameraFocus(String coordinates) {
        String[] c = coordinates.split(",");
        Point point = new Point(Double.parseDouble(c[0]), Double.parseDouble(c[1]));
        mapView.getMap().move(
                new CameraPosition(point, 18.0f, 0.0f, 0.0f),
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

    public void onBeginTripClick(View view) {
        if (selectedTransport == null) return;
        if (currentTripRent != null) {
            notification("У вас уже есть активная поездка");
            return;
        }
        view.setClickable(false);
        tripObserved.beginTrip(TripBeginDto.builder()
                .parkingId(selectedTransport.getParking().getId())
                .transportId(selectedTransport.getId())
                .build());
    }

    public void onRemoveSelectClick(View view) {
        this.removeSelectLayout();
    }

    private void removeSelectLayout() {
        scooterList.forEach(p -> {
            TransportDto t = (TransportDto) p.getUserData();
            assert t != null;
            if(t.getId().equals(selectedTransport.getId())) mapObjects.remove(p);
        });
        bicycleList.forEach(p -> {
            TransportDto t = (TransportDto) p.getUserData();
            assert t != null;
            if(t.getId().equals(selectedTransport.getId())) mapObjects.remove(p);
        });
        selectedLayout.removeAllViews();
        selectedLayout = null;
        selectedTransport = null;
    }

    public void onRemoveFilterClick(View view) {
        filterLayout.animate().translationYBy(animTranslationYBy).setDuration(animDuration);
    }

    @SuppressLint("CommitTransaction")
    public void onEndTripClick(View view) {
        makeEnd = true;

        EndTripDialog dialog = new EndTripDialog();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction();
        dialog.show(manager, "end-trip");
    }
}