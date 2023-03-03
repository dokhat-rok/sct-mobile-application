package com.sct.mobile.application.service.observed;

import com.sct.mobile.application.service.subscriber.TokenSubscriber;

public interface TokenObserved {

    void subscribeToken(TokenSubscriber subscriber);

    void unSubscribeToken();
}
