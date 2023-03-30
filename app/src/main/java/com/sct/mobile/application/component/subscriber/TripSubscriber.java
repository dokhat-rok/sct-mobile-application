package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.RentDto;

public interface TripSubscriber extends Subscriber {

    void acceptBeginTrip(RentDto rent);

    void errorBeginTrip(String error);

    void acceptEndTrip(RentDto rent);

    void errorEndTrip(String error);
}
