package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.RentDto;

import java.util.List;

public interface RentSubscriber extends Subscriber {

    void acceptGetAllRent(List<RentDto> rentList);

    void errorGetAllRent(String error);
}
