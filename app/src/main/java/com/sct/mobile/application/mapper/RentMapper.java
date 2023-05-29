package com.sct.mobile.application.mapper;

import com.sct.mobile.application.model.dto.ParkingDto;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.dto.RoutePointDto;
import com.sct.mobile.application.model.dto.TransportDto;
import com.sct.mobile.application.model.enums.TransportType;
import com.sct.mobile.application.model.view.RentView;
import com.yandex.mapkit.geometry.Geo;
import com.yandex.mapkit.geometry.Point;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class RentMapper {

    public static RentView dtoToView(RentDto rent) {
        ZonedDateTime beginTime = ZonedDateTime.parse(rent.getBeginTimeRent());
        ZonedDateTime endTime = null;
        if(rent.getEndTimeRent() != null) endTime = ZonedDateTime.parse(rent.getEndTimeRent());
        TransportDto ts = rent.getTransport();
        return RentView.builder()
                .id(rent.getId())
                .amount(parseAmount(rent.getAmount()))
                .date(parseDate(beginTime))
                .time(parseTime(beginTime, endTime))
                .distance(parseDistance(rent))
                .transport(parseTransport(ts.getType(), ts.getIdentificationNumber()))
                .build();
    }

    private static String parseAmount(Long amount) {
        return amount + " ₽";
    }

    private static String parseDate(ZonedDateTime time) {
        int day = time.getDayOfMonth();
        String month = switch (time.getMonth()) {
            case JANUARY -> "Январь";
            case FEBRUARY -> "Февраль";
            case MARCH -> "Март";
            case APRIL -> "Апрель";
            case MAY -> "Май";
            case JUNE -> "Июнь";
            case JULY -> "Июль";
            case AUGUST -> "Август";
            case SEPTEMBER -> "Сентябрь";
            case OCTOBER -> "Октябрь";
            case NOVEMBER -> "Ноябрь";
            case DECEMBER -> "Декабрь";
        };
        int year = time.getYear();
        String hour = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());
        if(hour.length() == 1) hour = "0" + hour;
        if(minute.length() == 1) minute = "0" + minute;
        return String.format(Locale.ROOT, "%d %s %d, %s:%s", day, month, year, hour, minute);
    }

    private static String parseTransport(TransportType type, String identification) {
        return type.getLabel() + " " + identification;
    }

    private static String parseDistance(RentDto rent) {
        double distance = 0d;
        if(rent.getRoutePoints() == null) return "";
        if(rent.getRoutePoints().size() < 2) {
            ParkingDto p1 = rent.getBeginParking();
            ParkingDto p2 = rent.getEndParking();
            if(p1 == null || p2 == null) return "";
            String c1 = p1.getCoordinates();
            String c2 = p2.getCoordinates();
            double x1 = Double.parseDouble(c1.split(",")[0]);
            double y1 = Double.parseDouble(c1.split(",")[1]);
            double x2 = Double.parseDouble(c2.split(",")[0]);
            double y2 = Double.parseDouble(c2.split(",")[1]);

            distance = Geo.distance(new Point(x1, y1), new Point(x2, y2));
        }
        else {
            List<RoutePointDto> points = rent.getRoutePoints();
            for(int i = 0; i < points.size() - 2; i++) {
                RoutePointDto p1 = points.get(i);
                RoutePointDto p2 = points.get(i + 1);
                distance += Geo.distance(
                        new Point(p1.getLatitude(), p1.getLongitude()),
                        new Point(p2.getLatitude(), p2.getLongitude()));
            }
        }
        return String.format(Locale.ROOT, "%6.2f км", distance / 1000);
    }

    private static String parseTime(ZonedDateTime begin, ZonedDateTime end) {
        if(begin == null || end == null) return "";
        Duration delta = Duration.between(begin.toLocalDateTime(), end.toLocalDateTime());
        long minutes = delta.toMinutes();
        long hours = minutes / 60;
        minutes %= 60;
        return String.format(Locale.ROOT, "%02d:%02d",
                hours,
                minutes);
    }
}