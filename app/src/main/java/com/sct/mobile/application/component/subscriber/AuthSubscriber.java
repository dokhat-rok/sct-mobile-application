package com.sct.mobile.application.component.subscriber;

import com.sct.mobile.application.model.dto.JwtDto;

public interface AuthSubscriber {

    void accept(JwtDto token);

    void error(String error);
}
