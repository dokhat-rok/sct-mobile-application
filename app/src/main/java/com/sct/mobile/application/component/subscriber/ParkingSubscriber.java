package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.ParkingDto;

import java.util.List;

public interface ParkingSubscriber extends Subscriber {

    void acceptGetAllParking(List<ParkingDto> parkingList);

    void errorGetAllParking(String error);
}
