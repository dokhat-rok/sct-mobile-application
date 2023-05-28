package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.UserDto;

public interface UserSubscriber extends Subscriber {

    default void acceptCurrent(UserDto user) {}

    default void errorUser(String error) {}

    default void acceptAdditionalBalance(UserDto user) {}

    default void errorAdditionalBalance(String error) {}

    default void acceptDelete() {}

    default void errorDelete(String error) {}
}
