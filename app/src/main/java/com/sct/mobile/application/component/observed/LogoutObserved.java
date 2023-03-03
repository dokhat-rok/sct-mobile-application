package com.sct.mobile.application.component.observed;

import com.sct.mobile.application.component.subscriber.LogoutSubscriber;

public interface LogoutObserved {

    void subscribeLogout(LogoutSubscriber subscriber);

    void unSubscribeLogout();
}
