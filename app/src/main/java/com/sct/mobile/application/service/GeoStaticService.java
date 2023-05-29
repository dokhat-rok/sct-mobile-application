package com.sct.mobile.application.service;

import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.dto.RoutePointDto;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.LocationManagerUtils;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Getter;
import lombok.Setter;

public class GeoStaticService {

    @Getter
    private static Point currentPoint;

    @Setter
    private static RentDto currentRent;

    private static final RoutePointService routePointService = new RoutePointService();

    private static Timer timer;
    private static Timer sentTimer;

    @Getter
    private static boolean isStart = false;

    public static void startSent() {
        isStart = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkGeo();
            }
        }, 0L, 100L);

        sentTimer = new Timer();
        sentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sentGeo();
            }
        }, 100L, 1000L);
    }

    public static void stopSent() {
        isStart = false;
        currentRent = null;
        timer.cancel();
        sentTimer.cancel();
    }

    private static void checkGeo() {
        currentPoint = Objects.requireNonNull(LocationManagerUtils.getLastKnownLocation())
                .getPosition();
    }

    private static void sentGeo() {
        Point point = currentPoint;
        RoutePointDto routePoint = RoutePointDto.builder()
                .rent(currentRent)
                .latitude(point.getLatitude())
                .longitude(point.getLongitude())
                .build();
        routePointService.savePoint(routePoint);
    }
}
