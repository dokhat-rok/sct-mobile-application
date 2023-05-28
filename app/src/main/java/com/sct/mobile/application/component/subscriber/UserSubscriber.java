package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.UserDto;

public interface UserSubscriber extends Subscriber {

    void acceptCurrent(UserDto user);

    void errorUser(String error);

    default void acceptDelete() {}

    default void errorDelete(String error) {}
}
