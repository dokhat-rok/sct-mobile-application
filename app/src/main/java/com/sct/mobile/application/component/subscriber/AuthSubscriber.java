package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.JwtDto;

public interface AuthSubscriber extends Subscriber {

    void acceptAuth(JwtDto token);

    void errorAuth(String error);
}
