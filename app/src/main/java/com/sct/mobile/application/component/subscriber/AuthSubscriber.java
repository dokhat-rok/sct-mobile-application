package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.JwtDto;

public interface AuthSubscriber {

    void acceptAuth(JwtDto token);

    void errorAuth(String error);
}
