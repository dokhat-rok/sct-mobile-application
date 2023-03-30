package com.sct.mobile.application.service.subscriber;

import com.sct.mobile.application.component.subscriber.Subscriber;

public interface TokenSubscriber extends Subscriber {

    void acceptToken();

    void errorToken(String error);
}
