package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.PriceDto;

public interface PriceSubscriber extends Subscriber {

    void acceptPrice(PriceDto price);

    void errorPrice(String error);
}
