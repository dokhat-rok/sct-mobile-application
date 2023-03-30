package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.UserDto;

public interface RegistrationSubscriber extends Subscriber {

    void acceptRegistration(UserDto user);

    void errorRegistration(String error);
}
