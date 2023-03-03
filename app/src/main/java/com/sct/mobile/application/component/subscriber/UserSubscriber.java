package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.UserDto;

public interface UserSubscriber {

    void acceptCurrent(UserDto user);

    void errorUser(String error);
}
