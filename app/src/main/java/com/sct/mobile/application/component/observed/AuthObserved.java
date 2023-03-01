package com.sct.mobile.application.component.observed;

import com.sct.mobile.application.component.subscriber.AuthSubscriber;

public interface AuthObserved {

    void subscribeAuth(AuthSubscriber subscriber);

    void unSubscribeAuth(AuthSubscriber subscriber);
}
