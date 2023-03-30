package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.TransportDto;
import com.sct.mobile.application.model.enums.TransportType;

import java.util.List;

public interface TransportSubscriber extends Subscriber {

    void acceptGetAllTransport(TransportType type, List<TransportDto> transportList);

    void errorGetAllTransport(String error);
}
